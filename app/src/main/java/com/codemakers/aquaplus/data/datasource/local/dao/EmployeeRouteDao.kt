package com.codemakers.aquaplus.data.datasource.local.dao

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
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTarifaEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoConcepto
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoTarifa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmUltimaLecturaHistorica
import com.codemakers.aquaplus.data.datasource.local.tables.RealmValorEstrato
import com.codemakers.aquaplus.data.models.response.EmployeeRouteConfigDto
import com.codemakers.aquaplus.data.models.response.EmployeeRouteResponseDto
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow

class EmployeeRouteDao(
    private val realm: Realm,
) {

    fun getAllEmployeeRouteFlow(): Flow<ResultsChange<RealmEmployeeRoute>> {
        return realm.query<RealmEmployeeRoute>().find().asFlow()
    }

    fun getAllEmployeeRouteConfigFlow(): Flow<ResultsChange<RealmEmployeeRouteConfig>> {
        return realm.query<RealmEmployeeRouteConfig>().find().asFlow()
    }

    fun getEmployeeRouteByIdFlow(id: Int): Flow<SingleQueryChange<RealmEmployeeRoute>> {
        return realm.query<RealmEmployeeRoute>("id == $0", id)
            .first()
            .asFlow()
    }

    fun getEmployeeRouteById(id: Int): RealmEmployeeRoute? {
        return realm.query<RealmEmployeeRoute>("id == $0", id)
            .first()
            .find()
    }

    fun getEmployeeRouteConfigByIdFlow(empresaId: Int): Flow<SingleQueryChange<RealmEmployeeRouteConfig>> {
        return realm.query<RealmEmployeeRouteConfig>("id == $0", empresaId)
            .first()
            .asFlow()
    }

    fun getEmployeeRouteConfigById(empresaId: Int): RealmEmployeeRouteConfig? {
        return realm.query<RealmEmployeeRouteConfig>("id == $0", empresaId)
            .first()
            .find()
    }

    suspend fun saveNewEmployeeRoute(
        data: EmployeeRouteResponseDto,
    ) {
        realm.write {
            val empresaData = RealmEmpresa().apply {
                id = data.empresaClienteContador.empresa.id
                nit = data.empresaClienteContador.empresa.nit
                codigo = data.empresaClienteContador.empresa.codigo
                nombre = data.empresaClienteContador.empresa.nombre
                activo = data.empresaClienteContador.empresa.activo
                direccion = RealmDireccion().apply {
                    ciudad = data.empresaClienteContador.empresa.direccion?.ciudad
                    descripcion = data.empresaClienteContador.empresa.direccion?.descripcion
                    departamento = data.empresaClienteContador.empresa.direccion?.departamento
                }
            }

            val deuda = data.empresaClienteContador.contador.deudas?.map {
                RealmDeuda().apply {
                    id = it.id
                    valor = it.valor
                    descripcion = it.descripcion
                }
            }

            val consumo = data.empresaClienteContador.contador.historicoConsumo?.map {
                RealmHistoricoConsumo().apply {
                    mes = it.mes
                    consumo = it.consumo
                    precio = it.precio
                }
            }

            val contadorData = RealmContador().apply {
                id = data.empresaClienteContador.contador.id
                serial = data.empresaClienteContador.contador.serial
                deudas = realmListOf<RealmDeuda>().apply { addAll(deuda.orEmpty()) }
                deudaAbonoSaldo = RealmDeudaAbonoSaldo().apply {
                    deudaTotal = data.empresaClienteContador.contador.deudaAbonoSaldo.deudaTotal
                    moraActual = data.empresaClienteContador.contador.deudaAbonoSaldo.moraActual
                    abonosTotal = data.empresaClienteContador.contador.deudaAbonoSaldo.abonosTotal
                }
                historicoConsumo =
                    realmListOf<RealmHistoricoConsumo>().apply { addAll(consumo.orEmpty()) }
                idTipoContador = data.empresaClienteContador.contador.idTipoContador
                nombreTipoContador = data.empresaClienteContador.contador.nombreTipoContador
                ultimaLecturaHistorica = RealmUltimaLecturaHistorica().apply {
                    id = data.empresaClienteContador.contador.ultimaLecturaHistorica?.id ?: 0
                    lectura =
                        data.empresaClienteContador.contador.ultimaLecturaHistorica?.lectura ?: 0
                    fechaLectura =
                        data.empresaClienteContador.contador.ultimaLecturaHistorica?.fechaLectura
                            ?: ""
                    idEccAsociado =
                        data.empresaClienteContador.contador.ultimaLecturaHistorica?.idEccAsociado
                            ?: 0
                    usuarioCreacion =
                        data.empresaClienteContador.contador.ultimaLecturaHistorica?.usuarioCreacion
                            ?: ""
                    personaCreacionNombre =
                        data.empresaClienteContador.contador.ultimaLecturaHistorica?.personaCreacionNombre
                            ?: ""
                    idPersonaClienteAsociada =
                        data.empresaClienteContador.contador.ultimaLecturaHistorica?.idPersonaClienteAsociada
                            ?: 0
                }
            }

            val direccionData = RealmDireccion().apply {
                ciudad = data.empresaClienteContador.personaCliente.direccion.ciudad
                descripcion = data.empresaClienteContador.personaCliente.direccion.descripcion
                departamento = data.empresaClienteContador.personaCliente.direccion.departamento
                corregimiento = data.empresaClienteContador.personaCliente.direccion.corregimiento
            }

            val personaClienteData = RealmPersonaCliente().apply {
                id = data.empresaClienteContador.personaCliente.id
                codigo = data.empresaClienteContador.personaCliente.codigo
                direccion = direccionData
                numeroCedula = data.empresaClienteContador.personaCliente.numeroCedula
                primerNombre = data.empresaClienteContador.personaCliente.primerNombre
                segundoNombre = data.empresaClienteContador.personaCliente.segundoNombre
                primerApellido = data.empresaClienteContador.personaCliente.primerApellido
                segundoApellido = data.empresaClienteContador.personaCliente.segundoApellido
            }

            val diasFacturaData = RealmDiasFactura().apply {
                diasVencida = data.empresaClienteContador.diasFactura?.diasVencida
            }

            val employeeRoute = RealmEmployeeRoute().apply {
                id = data.empresaClienteContador.id
                codFactura = data.empresaClienteContador.codFactura
                diasFactura = diasFacturaData
                empresa = empresaData
                contador = contadorData
                personaCliente = personaClienteData
            }

            copyToRealm(employeeRoute, updatePolicy = UpdatePolicy.ALL)
        }
    }

    suspend fun saveNewEmployeeRouteConfig(
        data: EmployeeRouteConfigDto,
    ) {
        realm.write {
            val employeeRouteConfig = RealmEmployeeRouteConfig().apply {
                id = data.config?.empresa?.id ?: 0
                config = RealmConfig().apply {
                    empresa = RealmEmpresa().apply {
                        id = data.config?.empresa?.id
                        nit = data.config?.empresa?.nit
                        codigo = data.config?.empresa?.codigo
                        nombre = data.config?.empresa?.nombre
                        activo = data.config?.empresa?.activo
                        direccion = RealmDireccion().apply {
                            ciudad = data.config?.empresa?.direccion?.ciudad
                            descripcion = data.config?.empresa?.direccion?.descripcion
                            departamento = data.config?.empresa?.direccion?.departamento
                            corregimiento = data.config?.empresa?.direccion?.corregimiento
                        }
                        logoEmpresa = RealmGenericEmpresa().apply {
                            nombre = data.config?.empresa?.logoEmpresa?.nombre
                            imagen = data.config?.empresa?.logoEmpresa?.imagen
                        }
                        puntosPago = realmListOf<RealmGenericEmpresa>().apply {
                            data.config?.empresa?.puntosPago?.forEach {
                                add(
                                    RealmGenericEmpresa().apply {
                                        nombre = it.nombre
                                        imagen = it.imagen
                                    }
                                )
                            }
                        }
                        codigoQr = RealmGenericEmpresa().apply {
                            nombre = data.config?.empresa?.codigoQr?.nombre
                            imagen = data.config?.empresa?.codigoQr?.imagen
                        }
                    }
                    tarifasEmpresa = realmListOf<RealmTarifaEmpresa>().apply {
                        data.config?.tarifasEmpresa?.forEach {
                            add(
                                RealmTarifaEmpresa().apply {
                                    id = it.id
                                    empresa = it.empresa
                                    tipoTarifa = RealmTipoTarifa().apply {
                                        id = it.tipoTarifa?.id
                                        codigo = it.tipoTarifa?.codigo
                                        nombre = it.tipoTarifa?.nombre
                                        descripcion = it.tipoTarifa?.descripcion
                                    }
                                    conceptos = realmListOf<RealmConcepto>().apply {
                                        it.conceptos?.forEach { concepto ->
                                            add(
                                                RealmConcepto().apply {
                                                    id = concepto.id
                                                    valor = concepto.valor
                                                    indCalcularMc = concepto.indCalcularMc
                                                    tipoConcepto = RealmTipoConcepto().apply {
                                                        id = concepto.tipoConcepto?.id
                                                        codigo = concepto.tipoConcepto?.codigo
                                                        descripcion =
                                                            concepto.tipoConcepto?.descripcion
                                                    }
                                                    valoresEstrato =
                                                        realmListOf<RealmValorEstrato>().apply {
                                                            concepto.valoresEstrato?.forEach { valorEstrato ->
                                                                add(
                                                                    RealmValorEstrato().apply {
                                                                        id = valorEstrato.id
                                                                        valor = valorEstrato.valor
                                                                        estrato =
                                                                            valorEstrato.estrato
                                                                    }
                                                                )
                                                            }
                                                        }
                                                }
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                catalogos = RealmCatalogos().apply {
                    tiposTarifa = realmListOf<RealmTipoTarifa>().apply {
                        data.catalogos?.tiposTarifa?.forEach {
                            add(
                                RealmTipoTarifa().apply {
                                    id = it.id
                                    codigo = it.codigo
                                    nombre = it.nombre
                                    descripcion = it.descripcion
                                }
                            )
                        }
                    }
                    tiposConcepto = realmListOf<RealmTipoConcepto>().apply {
                        data.catalogos?.tiposConcepto?.forEach {
                            add(
                                RealmTipoConcepto().apply {
                                    id = it.id
                                    codigo = it.codigo
                                    descripcion = it.descripcion
                                }
                            )
                        }
                    }
                }
            }
            copyToRealm(employeeRouteConfig, updatePolicy = UpdatePolicy.ALL)
        }
    }
}