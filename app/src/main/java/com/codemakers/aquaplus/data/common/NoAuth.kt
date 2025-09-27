package com.codemakers.aquaplus.data.common

/**
 * Anotación para indicar que una petición de Retrofit
 * no debe incluir el token de autorización por defecto del interceptor.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class NoAuth