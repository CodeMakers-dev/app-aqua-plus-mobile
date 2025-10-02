package com.codemakers.aquaplus.data.di

import androidx.room.Room
import com.codemakers.aquaplus.data.datasource.local.AppDatabase
import com.codemakers.aquaplus.data.datasource.local.dao.EmployeeRouteDao
import com.codemakers.aquaplus.data.datasource.local.dao.ReadingFormDataDao
import com.codemakers.aquaplus.data.datasource.local.tables.RealmCatalogos
import com.codemakers.aquaplus.data.datasource.local.tables.RealmConcepto
import com.codemakers.aquaplus.data.datasource.local.tables.RealmConfig
import com.codemakers.aquaplus.data.datasource.local.tables.RealmContador
import com.codemakers.aquaplus.data.datasource.local.tables.RealmDeuda
import com.codemakers.aquaplus.data.datasource.local.tables.RealmDeudaAbonoSaldo
import com.codemakers.aquaplus.data.datasource.local.tables.RealmDiasFactura
import com.codemakers.aquaplus.data.datasource.local.tables.RealmDireccion
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEmployeeRoute
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEmployeeRouteConfig
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmGenericEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmHistoricoConsumo
import com.codemakers.aquaplus.data.datasource.local.tables.RealmPersonaCliente
import com.codemakers.aquaplus.data.datasource.local.tables.RealmReadingFormData
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTarifaEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoConcepto
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoTarifa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmUltimaLecturaHistorica
import com.codemakers.aquaplus.data.datasource.local.tables.RealmValorEstrato
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val daoModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "AquaPlusDB")
            .fallbackToDestructiveMigration(false)
            .addMigrations()
            .build()
    }
    single { get<AppDatabase>().userDao() }

    single {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                RealmEmployeeRoute::class,
                RealmDiasFactura::class,
                RealmEmpresa::class,
                RealmContador::class,
                RealmDeuda::class,
                RealmDeudaAbonoSaldo::class,
                RealmHistoricoConsumo::class,
                RealmUltimaLecturaHistorica::class,
                RealmPersonaCliente::class,
                RealmDireccion::class,
                RealmEmployeeRouteConfig::class,
                RealmConfig::class,
                RealmTarifaEmpresa::class,
                RealmConcepto::class,
                RealmTipoConcepto::class,
                RealmValorEstrato::class,
                RealmTipoTarifa::class,
                RealmCatalogos::class,
                RealmReadingFormData::class,
                RealmGenericEmpresa::class,
            )
        )
            .name("acueducto-app.realm")
            .schemaVersion(5)
            // .migration(MyMigration())
            .build()
        Realm.open(config)
    }
    single { EmployeeRouteDao(get()) }
    single { ReadingFormDataDao(get()) }
}

