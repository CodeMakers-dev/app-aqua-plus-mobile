package com.codemakers.aquaplus.data.datasource.local.dao

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
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEstadoMedidor
import com.codemakers.aquaplus.data.datasource.local.tables.RealmGenericEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmHistoricoConsumo
import com.codemakers.aquaplus.data.datasource.local.tables.RealmParametrosEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmPersonaCliente
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTarifaEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoConcepto
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoTarifa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoUso
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoUsoWrapper
import com.codemakers.aquaplus.data.datasource.local.tables.RealmUltimaFactura
import com.codemakers.aquaplus.data.datasource.local.tables.RealmUltimaLecturaHistorica
import com.codemakers.aquaplus.data.datasource.local.tables.RealmValorEstrato
import com.codemakers.aquaplus.data.datasource.local.tables.RealmValorMc
import com.codemakers.aquaplus.data.models.response.EmployeeRouteConfigDto
import com.codemakers.aquaplus.data.models.response.EmployeeRouteResponseDto
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EmployeeRouteDao(
    private val realm: Realm,
) {

    suspend fun getAllEmployeeRouteFlow(): Flow<ResultsChange<RealmEmployeeRoute>> {
        return withContext(Dispatchers.IO) { realm.query<RealmEmployeeRoute>().find().asFlow() }
    }

    suspend fun getAllEmployeeRouteConfigFlow(): Flow<ResultsChange<RealmEmployeeRouteConfig>> {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRouteConfig>().find().asFlow()
        }
    }

    suspend fun getEmployeeRouteByIdFlow(id: Int): RealmEmployeeRoute? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRoute>("id == $0", id)
                .first()
                .find()
        }
    }

    suspend fun getEmployeeRouteById(id: Int): RealmEmployeeRoute? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRoute>("id == $0", id)
                .first()
                .find()
        }
    }

    suspend fun getEmployeeRouteConfigByIdFlow(empresaId: Int):RealmEmployeeRouteConfig? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRouteConfig>("id == $0", empresaId)
                .first()
                .find()
        }
    }

    suspend fun getEmployeeRouteConfigById(empresaId: Int): RealmEmployeeRouteConfig? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRouteConfig>("id == $0", empresaId)
                .first()
                .find()
        }
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
                fechaInstalacion = data.empresaClienteContador.contador.fechaInstalacion
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

            val ultimaFacturaData = RealmUltimaFactura().apply {
                fecha = data.empresaClienteContador.ultimaFactura?.fecha
                precio = data.empresaClienteContador.ultimaFactura?.precio
            }

            val employeeRoute = RealmEmployeeRoute().apply {
                id = data.empresaClienteContador.id
                codFactura = data.empresaClienteContador.codFactura
                diasFactura = diasFacturaData
                empresa = empresaData
                contador = contadorData
                personaCliente = personaClienteData
                ultimaFactura = ultimaFacturaData
            }

            copyToRealm(employeeRoute, updatePolicy = UpdatePolicy.ALL)
        }
    }

    suspend fun saveNewEmployeeRouteConfig(
        data: EmployeeRouteConfigDto,
    ) {
        realm.write {
            val empresaData = RealmEmpresa().apply {
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
                    val puntosPagoList = data.config?.empresa?.puntosPago?.map {
                        RealmGenericEmpresa().apply {
                            nombre = it.nombre
                            imagen = it.imagen
                        }
                    }
                    addAll(puntosPagoList.orEmpty())
                }
                codigoQr = RealmGenericEmpresa().apply {
                    nombre = data.config?.empresa?.codigoQr?.nombre
                    imagen = data.config?.empresa?.codigoQr?.imagen
                }
                correoEmpresa = data.config?.empresa?.correoEmpresa
                telefonoEmpresa = data.config?.empresa?.telefonoEmpresa
                piePagina = data.config?.empresa?.piePagina
                avisoFactura = data.config?.empresa?.avisoFactura
            }
            val tipoUsoData = data.config?.tipoUso?.let {
                RealmTipoUsoWrapper().apply {
                    this.data = realmListOf<RealmTipoUso>().apply {
                        val tipoUsoList = it.data?.map { tipoUso ->
                            RealmTipoUso().apply {
                                id = tipoUso.id
                                activo = tipoUso.activo
                                codigo = tipoUso.codigo
                                nombre = tipoUso.nombre
                                descripcion = tipoUso.descripcion
                            }
                        }
                        addAll(tipoUsoList.orEmpty())
                    }
                }
            }
            val estadosMedidorData = realmListOf<RealmEstadoMedidor>().apply {
                val estadosData = data.config?.estadosMedidor?.map { estado ->
                    RealmEstadoMedidor().apply {
                        id = estado.id
                        codigo = estado.codigo
                        descripcion = estado.descripcion
                    }
                }
                addAll(estadosData.orEmpty())
            }
            val tarifasEmpresaData = realmListOf<RealmTarifaEmpresa>().apply {
                val tarifasList = data.config?.tarifasEmpresa?.map { tarifasEmpresa ->
                        RealmTarifaEmpresa().apply {
                            id = tarifasEmpresa.id
                            empresa = tarifasEmpresa.empresa
                            valorMc = realmListOf<RealmValorMc>().apply {
                                val valorMcData = tarifasEmpresa.valorMc?.map { valorMc ->
                                    RealmValorMc().apply {
                                        id = valorMc.id
                                        rango = valorMc.rango
                                        valor = valorMc.valor
                                        codigo = valorMc.codigo
                                        nombre = valorMc.nombre
                                        estrato = valorMc.estrato
                                        tipoUso = valorMc.tipoUso?.let { tipoUso ->
                                            RealmTipoUso().apply {
                                                id = tipoUso.id
                                                activo = tipoUso.activo
                                                codigo = tipoUso.codigo
                                                nombre = tipoUso.nombre
                                                descripcion = tipoUso.descripcion
                                            }
                                        }
                                    }
                                }
                                addAll(valorMcData.orEmpty())
                            }
                            tipoTarifa = RealmTipoTarifa().apply {
                                id = tarifasEmpresa.tipoTarifa?.id
                                codigo = tarifasEmpresa.tipoTarifa?.codigo
                                nombre = tarifasEmpresa.tipoTarifa?.nombre
                                descripcion = tarifasEmpresa.tipoTarifa?.descripcion
                            }
                            conceptos = realmListOf<RealmConcepto>().apply {
                                val conceptosList = tarifasEmpresa.conceptos?.map { concepto ->
                                    RealmConcepto().apply {
                                        id = concepto.id
                                        valor = concepto.valor
                                        indCalcularMc = concepto.indCalcularMc
                                        tipoConcepto = RealmTipoConcepto().apply {
                                            id = concepto.tipoConcepto?.id
                                            codigo = concepto.tipoConcepto?.codigo
                                            descripcion = concepto.tipoConcepto?.descripcion
                                        }
                                        valoresEstrato = realmListOf<RealmValorEstrato>().apply {
                                            val valoresEstratoList =
                                                    concepto.valoresEstrato?.map { valorEstrato ->
                                                        RealmValorEstrato().apply {
                                                            id = valorEstrato.id
                                                            valor = valorEstrato.valor
                                                            estrato = valorEstrato.estrato
                                                        }
                                                    }
                                            addAll(valoresEstratoList.orEmpty())
                                            }
                                    }
                                }
                                addAll(conceptosList.orEmpty())
                            }
                        }
                }
                addAll(tarifasList.orEmpty())
            }
            val parametrosEmpresaData = data.config?.parametrosEmpresa?.let {
                RealmParametrosEmpresa().apply {
                    consuBasico = it.consuBasico
                    diasVencida = it.diasVencida
                    periodosInm = it.periodosInm
                    periodosVig = it.periodosVig
                    periodosFact = it.periodosFact
                    consuSuntuario = it.consuSuntuario
                    consuComplementario = it.consuComplementario
                }
            }
            val employeeRouteConfig = RealmEmployeeRouteConfig().apply {
                id = data.config?.empresa?.id ?: 0
                config = RealmConfig().apply {
                    empresa = empresaData
                    tipoUso = tipoUsoData
                    estadosMedidor = estadosMedidorData
                    tarifasEmpresa = tarifasEmpresaData
                    parametrosEmpresa = parametrosEmpresaData
                }
            }
            copyToRealm(employeeRouteConfig, updatePolicy = UpdatePolicy.ALL)
        }
    }
}