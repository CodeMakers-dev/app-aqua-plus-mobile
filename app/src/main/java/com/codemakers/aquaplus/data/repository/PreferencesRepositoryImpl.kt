package com.codemakers.aquaplus.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.codemakers.aquaplus.domain.repository.PreferencesRepository
import com.google.gson.Gson

const val REFRESH_TOKEN = "refresh_token"
const val TOKEN = "token"
const val USER = "user"

class PreferencesRepositoryImpl(
    private val context: Context,
    private val gson: Gson
) : PreferencesRepository {

    private fun getPreferencesFile(): SharedPreferences {
        val preferenceFileName = "${context.packageName}.PREFERENCE_FILE_KEY"
        return context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
    }

    private fun getEditor(): SharedPreferences.Editor {
        val preferences = getPreferencesFile()
        return preferences.edit()
    }

    override fun getString(key: String): String? {
        val sharedFile = getPreferencesFile()
        return sharedFile.getString(key, null)
    }

    override fun getString(key: String, defaultValue: String): String? {
        val sharedFile = getPreferencesFile()
        return sharedFile.getString(key, defaultValue)
    }

    override fun <T> getObject(key: String, classOfT: Class<T>): T? {
        return try {
            gson.fromJson(getString(key), classOfT)
        } catch (_: Exception) {
            null
        }
    }

    override fun setObject(key: String, data: Any) {
        try {
            set(key, data)
        } catch (_: Exception) {
        }
    }

    override fun set(key: String, data: Any) {
        val editor = getEditor()
        editor.putString(key, gson.toJson(data))
        editor.commit()
    }

    override fun setString(key: String, data: String) {
        val editor = getEditor()
        editor.putString(key, data)
        editor.commit()
    }

    override fun remove(key: String) {
        val editor = getEditor()
        editor.remove(key)
        editor.commit()
    }

    override fun contains(key: String): Boolean {
        return getPreferencesFile().contains(key)
    }
}