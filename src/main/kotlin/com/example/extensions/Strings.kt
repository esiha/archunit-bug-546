package com.example.extensions

fun String.requireMaximumLength(maxLength: Int) {
    require(length <= maxLength) { "Value too long (max $maxLength characters): '$this'" }
}