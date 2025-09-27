package com.codemakers.aquaplus.domain.repository

interface PreferencesRepository {

    fun getString(key: String): String?

    fun getString(key: String, defaultValue: String): String?

    fun <T> getObject(key: String, classOfT: Class<T>): T?

    fun setObject(key: String, data: Any)

    fun set(key: String, data: Any)

    fun setString(key: String, data: String)

    fun remove(key: String)

    fun contains(key: String): Boolean

}