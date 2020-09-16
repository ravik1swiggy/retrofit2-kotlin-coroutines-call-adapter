package com.melegy.retrofitcoroutines

import java.math.BigDecimal
import java.security.MessageDigest

/**
 * Created by Sunil on 7/2/17.
 */

class KotlinExtensions

inline fun <T> List<T>.isNotEmpty(block: (List<T>) -> Unit): Unit {
	if (this.isNotEmpty()) {
		block(this)
	}
}

fun <T> List<T>.first(): T {
	return this[0]
}

fun <T> List<T>.last(): T {
	return this[lastIndex]
}

inline fun <T> List<T>.first(action: (T) -> Unit): Unit {
	action(first())
}

inline fun <T> List<T>.last(action: (T) -> Unit): Unit {
	action(last())
}

fun <T> List<T>.safeGet(index: Int): T? {

	if (index in indices) {
		return this[index]
	}

	return null
}

fun <T> List<T>.safeGet(index: Int, defaultValue: T): T? {

	if (index in indices) {
		return this[index]
	}

	return defaultValue
}

fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
	return if (p1 != null && p2 != null) block(p1, p2) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
	p1: T1?,
	p2: T2?,
	p3: T3?,
	block: (T1, T2, T3) -> R?
): R? {
	return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
	p1: T1?,
	p2: T2?,
	p3: T3?,
	p4: T4?,
	block: (T1, T2, T3, T4) -> R?
): R? {
	return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(
	p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?,
	block: (T1, T2, T3, T4, T5) -> R?
): R? {
	return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(
		p1,
		p2,
		p3,
		p4,
		p5
	) else null
}

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, R : Any> safeLet(
	p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, p6: T6?,
	block: (T1, T2, T3, T4, T5, T6) -> R?
): R? {
	return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null && p6 != null) block(
		p1,
		p2,
		p3,
		p4,
		p5,
		p6
	) else null
}

fun String?.safeToLong(defaultValue: Long = -1): Long {

	var value = defaultValue
	try {
		if (this != null) {
			value = this.toLong()
		}
	} catch (t: Throwable) {

	}
	return value
}

fun String?.safeToInt(defaultValue: Int = -1): Int {

	var value = defaultValue
	try {
		if (this != null) {
			value = this.toInt()
		}
	} catch (t: Throwable) {

	}
	return value
}

fun String?.safeToDouble(defaultValue: Double = -1.0): Double {

	var value = defaultValue
	try {
		if (this != null) {
			value = this.toDouble()
		}
	} catch (t: Throwable) {

	}
	return value
}

fun String?.safeToBoolean(defaultValue: Boolean = false): Boolean {

	var value = defaultValue
	try {
		if (!this.isNullOrBlank()) {
			value = this.toBoolean()
		}
	} catch (t: Throwable) {
	}
	return value
}

fun <T> ifElseLet(a: T?, condition: Boolean, ifBlock: ((T) -> Unit)?, elseBlock: (() -> Unit)?) {

	if (a != null && condition) {
		ifBlock?.invoke(a)
	} else {
		elseBlock?.invoke()
	}
}

inline fun <T> T.let(condition: Boolean, block: (T) -> Unit) {

	if (condition) {
		block(this)
	}
}

inline fun <T> T.let(condition: Boolean, ifBlock: ((T) -> Unit), elseBlock: (() -> Unit)) {

	if (condition) {
		ifBlock(this)
	} else {
		elseBlock()
	}
}

inline infix fun <reified T> Boolean.then(function: () -> T) = if (this) function() else null

fun Boolean?.isTrue(): Boolean {
	return this == true
}

fun Int?.isGreaterThan(num: Int): Boolean {
	return this != null && this > num
}

fun Int?.isLesserThan(num: Int): Boolean {
	return this != null && this < num
}

fun Float?.isGreaterThan(num: Float): Boolean {
	return this != null && this > num
}

fun Float?.isLesserThan(num: Float): Boolean {
	return this != null && this < num
}

fun Boolean?.isFalse(): Boolean {
	return this == null || this == false
}

fun Double.round(places: Int, roundingMode: Int = BigDecimal.ROUND_HALF_UP): Double {
	return BigDecimal(this).setScale(places, roundingMode).toDouble()
}

fun <T> List<T>.trimToTwo(): List<T> {
	if (this.size > 2) {
		return this.subList(0, 2)
	}
	return this
}

fun String.toMD5(): String {
	return runCatching {
		MessageDigest.getInstance("MD5").digest(this.toByteArray()).toHex()
	}.getOrElse { this }
}

fun ByteArray.toHex(): String {
	return joinToString("") { "%02x".format(it) }
}
