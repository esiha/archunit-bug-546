package com.example.extensions

import java.lang.reflect.Field
import kotlin.reflect.KClass

fun KClass<*>.declaredField(name: String): Field? = this.java.declaredFields.find { it.name == name }

inline fun <reified A : Annotation> Field.findAnnotation(): A? = getDeclaredAnnotation(A::class.java)