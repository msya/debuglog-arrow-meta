package com.debuglog

import arrow.meta.Meta
import arrow.meta.Plugin
import arrow.meta.invoke
import arrow.meta.phases.analysis.body
import arrow.meta.phases.analysis.bodySourceAsExpression
import arrow.meta.quotes.Transform
import arrow.meta.quotes.namedFunction
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

const val INT = "Int"
const val LONG = "Long"
const val DEBUG_LOG = "DebugLog"

val Meta.debugLog: Plugin
    get() =
        "DebugLog" {
            meta(
                    namedFunction({ validateFunction() }) { c: KtNamedFunction ->
                        Transform.replace(
                                replacing = c,
                                newDeclaration = replace(c).function
                        )
                    }
            )
        }


/**
 * Match a function that fits this scenario.
 *
 * - Function has 1 param that is of Int type.
 * - Function has a Long return type.
 * - Function has a DebugLog annotation.
 */
private fun KtNamedFunction.validateFunction(): Boolean =
        hasOneIntParam() && hasLongReturnType() && hasAnnotation(DEBUG_LOG)

/**
 * Check function has 1 param that is of Int type.
 */
private fun KtNamedFunction.hasOneIntParam(): Boolean =
        valueParameterList?.parameters?.size == 1 &&
                valueParameterList?.parameters?.first()?.typeReference?.text == INT

/**
 * Check function is returning a Long.
 */
private fun KtNamedFunction.hasLongReturnType(): Boolean =
        hasDeclaredReturnType() && typeReference?.text == LONG

/**
 * Check function has [annotationNames] as an annotation.
 */
fun KtAnnotated.hasAnnotation(
        vararg annotationNames: String
): Boolean {
    val names = annotationNames.toHashSet()
    val predicate: (KtAnnotationEntry) -> Boolean = {
        it.typeReference
                ?.typeElement
                ?.safeAs<KtUserType>()
                ?.referencedName in names
    }
    return annotationEntries.any(predicate)
}

/**
 * Replace [function] with a new function that prints how long the function
 * took to execute.
 */
fun replace(function: KtNamedFunction): String {
    val functionName = function.name
    val paramName = function.valueParameters.first().name
    val functionBody = function.body()?.bodySourceAsExpression()

    return """
|//metadebug
|fun ${functionName}(${paramName}: Int): Long {
              |   println("-> $functionName(${paramName}=$${paramName})")
              |   val startTime = System.currentTimeMillis()
              |   val result = $functionBody
              |   val timeToRun = System.currentTimeMillis() - startTime
              |   println("<- ${functionName}[ran in ${'$'}timeToRun ms]")
              |   return result
              | }"""
}
