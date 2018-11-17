package net.cabii.rose.utils

import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Paths

private fun String.isUNCPath() = (startsWith("//") || startsWith("\\\\")) && length > 4

inline infix fun <R> String.fromClasspath(transform: InputStream.() -> R): R {
    val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(this)
    return stream.transform()
}

val String.file: File
    get() = when {
        startsWith("~/") -> File((System.getProperty("user.home") ?: "") + substring(1))
        isUNCPath() -> Paths.get(URL("file:" + this).toURI()).toFile()
        else -> File(this)
    }

operator fun StringBuilder.plusAssign(other: Any?) {
    this.append(other)
}
operator fun StringBuilder.plusAssign(other: String?) {
    this.append(other)
}

fun String.padAround(paddedLength: Int, with: String = " "): String {
    if ( paddedLength < 0 ) throw IllegalArgumentException( "length must not be negative!" )
    if ( with.isEmpty() ) throw IllegalArgumentException( "with must be a string of length 1 or more" )
    if ( length >= paddedLength ) return this

    val missingLen = paddedLength - length
    val ret = StringBuilder( paddedLength )
    val leftPadAmount = missingLen / 2
    val leftPadTimes = leftPadAmount / with.length
    for (i in 1..leftPadTimes) ret += with
    ret += with.substring( 0, leftPadAmount % with.length )
    ret += this
    val rightPadAmount = ( ( missingLen + 1 ) / 2 )
    val rightPadTimes = rightPadAmount / with.length
    for ( i in 1..rightPadTimes ) ret += with
    ret += with.substring( 0, rightPadAmount % with.length )

    return ret.toString()
}
