package com.debuglog

import arrow.meta.plugin.testing.CompilerTest
import arrow.meta.plugin.testing.CompilerTest.Companion.source
import arrow.meta.plugin.testing.assertThis
import org.junit.Test

class DebugLogMetaPluginTest {

    companion object {

        val debuglog = """
                |annotation class DebugLog
                |
                |@DebugLog
                |fun prime(n: Int): Long = listOf(1L,2L,3L).take(n).last()
                | """.trimMargin().trim().source

        val expectedOutput = """
                |annotation class DebugLog
                |
                  |//metadebug
                  |fun prime(n: Int): Long {
                  |   println("-> prime(n=${'$'}n)")
                  |   val startTime = System.currentTimeMillis()
                  |   val result = listOf(1L,2L,3L).take(n).last()
                  |   val timeToRun = System.currentTimeMillis() - startTime
                  |   println("<- prime[ran in ${'$'}timeToRun ms]")
                  |   return result
                  | }""".trimMargin().trim().source
    }

    @Test
    fun `should print function time execution`() {
        assertThis(CompilerTest(
                config = { listOf(addMetaPlugins(DebugLogMetaPlugin())) },
                code = { debuglog },
                assert = { quoteOutputMatches(expectedOutput) }
        ))
    }

}
