package com.codemakers.aquaplus.data.datasource.local.tables

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmAuthSession : RealmObject {
    @PrimaryKey
    var personId: Int = 0
    var token: String = ""
    var refreshToken: String = ""
    var username: String = ""
}
