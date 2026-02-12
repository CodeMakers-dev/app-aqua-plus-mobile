package com.codemakers.aquaplus.data.datasource.local.dao

import com.codemakers.aquaplus.data.datasource.local.tables.RealmAuthSession
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthSessionDao(
    private val realm: Realm,
) {

    suspend fun saveAuthSession(personId: Int, token: String, refreshToken: String) {
        realm.write {
            val authSession = RealmAuthSession().apply {
                this.personId = personId
                this.token = token
                this.refreshToken = refreshToken
            }
            copyToRealm(authSession, updatePolicy = UpdatePolicy.ALL)
        }
    }

    suspend fun updateToken(personId: Int, token: String) {
        realm.write {
            val authSession = query<RealmAuthSession>("personId == $0", personId)
                .first()
                .find()
            authSession?.token = token
        }
    }

    suspend fun getAuthSession(personId: Int): RealmAuthSession? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmAuthSession>("personId == $0", personId)
                .first()
                .find()
        }
    }
}
