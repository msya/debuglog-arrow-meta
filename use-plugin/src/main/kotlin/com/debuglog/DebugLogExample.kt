package com.debuglog

annotation class DebugLog

@DebugLog
fun prime(n: Int): Long = listOf(1L,2L,3L).take(n).last()

fun main() {
  prime(10)
}
