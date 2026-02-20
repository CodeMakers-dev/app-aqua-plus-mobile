package com.codemakers.aquaplus.data.datasource.local.dao

import com.codemakers.aquaplus.data.datasource.local.tables.RealmConcepto
import com.codemakers.aquaplus.data.datasource.local.tables.RealmConfig
import com.codemakers.aquaplus.data.datasource.local.tables.RealmContador
import com.codemakers.aquaplus.data.datasource.local.tables.RealmDeudaAbonoSaldo
import com.codemakers.aquaplus.data.datasource.local.tables.RealmDeudaCliente
import com.codemakers.aquaplus.data.datasource.local.tables.RealmDireccion
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEmployeeRoute
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEmployeeRouteConfig
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmEstadoMedidor
import com.codemakers.aquaplus.data.datasource.local.tables.RealmGenericEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmHistoricoConsumo
import com.codemakers.aquaplus.data.datasource.local.tables.RealmParametrosEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmRangoConsumo
import com.codemakers.aquaplus.data.datasource.local.tables.RealmPersonaCliente
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTarifaContador
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTarifaEmpresa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoConcepto
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoTarifa
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoUso
import com.codemakers.aquaplus.data.datasource.local.tables.RealmTipoUsoWrapper
import com.codemakers.aquaplus.data.datasource.local.tables.RealmUltimaFactura
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

    suspend fun getAllEmployeeRouteFlow(personId: Int): List<RealmEmployeeRoute> {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRoute>("personId == $0", personId).find()
        }
    }

    suspend fun getAllEmployeeRouteConfigFlow(personId: Int): Flow<ResultsChange<RealmEmployeeRouteConfig>> {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRouteConfig>("personId == $0", personId).find().asFlow()
        }
    }

    suspend fun getEmployeeRouteByIdFlow(id: Int, personId: Int): RealmEmployeeRoute? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRoute>("id == $0 AND personId == $1", id, personId)
                .first()
                .find()
        }
    }

    suspend fun getEmployeeRouteById(id: Int, personId: Int): RealmEmployeeRoute? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRoute>("id == $0 AND personId == $1", id, personId)
                .first()
                .find()
        }
    }

    suspend fun getEmployeeRouteConfigByIdFlow(empresaId: Int, personId: Int):RealmEmployeeRouteConfig? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRouteConfig>("id == $0 AND personId == $1", empresaId, personId)
                .first()
                .find()
        }
    }

    suspend fun getEmployeeRouteConfigById(empresaId: Int, personId: Int): RealmEmployeeRouteConfig? {
        return withContext(Dispatchers.IO) {
            realm.query<RealmEmployeeRouteConfig>("id == $0 AND personId == $1", empresaId, personId)
                .first()
                .find()
        }
    }

    suspend fun saveNewEmployeeRoute(
        data: EmployeeRouteResponseDto,
        personId: Int,
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

            val tarifaContadorList = data.empresaClienteContador.contador.tarifaContador?.map {
                RealmTarifaContador().apply {
                    aplica = it.aplica
                    idTipoTarifa = it.idTipoTarifa
                }
            }

            val consumo = data.empresaClienteContador.contador.historicoConsumo?.map {
                RealmHistoricoConsumo().apply {
                    mes = it.mes
                    consumo = it.consumo
                    precio = it.precio
                    fechaLectura = it.fechaLectura
                }
            }

            val contadorData = RealmContador().apply {
                id = data.empresaClienteContador.contador.id
                nuid = data.empresaClienteContador.contador.nuid
                serial = data.empresaClienteContador.contador.serial
                digitos = data.empresaClienteContador.contador.digitos
                estrato = data.empresaClienteContador.contador.estrato
                idTipoUso = data.empresaClienteContador.contador.idTipoUso
                matricula = data.empresaClienteContador.contador.matricula
                nombreTipoUso = data.empresaClienteContador.contador.nombreTipoUso
                ultimaLectura = data.empresaClienteContador.contador.ultimaLectura
                idTipoContador = data.empresaClienteContador.contador.idTipoContador
                tarifaContador = realmListOf<RealmTarifaContador>().apply { addAll(tarifaContadorList.orEmpty()) }
                deudaAbonoSaldo = RealmDeudaAbonoSaldo().apply {
                    deudaTotal = data.empresaClienteContador.contador.deudaAbonoSaldo.deudaTotal
                    moraActual = data.empresaClienteContador.contador.deudaAbonoSaldo.moraActual
                    abonosTotal = data.empresaClienteContador.contador.deudaAbonoSaldo.abonosTotal
                }
                promedioConsumo = data.empresaClienteContador.contador.promedioConsumo
                fechaInstalacion = data.empresaClienteContador.contador.fechaInstalacion
                historicoConsumo = realmListOf<RealmHistoricoConsumo>().apply { addAll(consumo.orEmpty()) }
                idEstadoContador = data.empresaClienteContador.contador.idEstadoContador
                lecturaProyectada = data.empresaClienteContador.contador.lecturaProyectada
                nombreTipoContador = data.empresaClienteContador.contador.nombreTipoContador
                nombreEstadoContador = data.empresaClienteContador.contador.nombreEstadoContador
            }

            val direccionData = RealmDireccion().apply {
                ciudad = data.empresaClienteContador.personaCliente.direccion.ciudad
                descripcion = data.empresaClienteContador.personaCliente.direccion.descripcion
                departamento = data.empresaClienteContador.personaCliente.direccion.departamento
                corregimiento = data.empresaClienteContador.personaCliente.direccion.corregimiento
            }

            val deudaClienteList = data.empresaClienteContador.personaCliente.deudaCliente?.map {
                RealmDeudaCliente().apply {
                    nuevoSaldo = it.nuevoSaldo
                    valorCuota = it.valorCuota
                    idTipoDeuda = it.idTipoDeuda
                    numeroCuotas = it.numeroCuotas
                    codigoTipoDeuda = it.codigoTipoDeuda
                    nombreTipoDeuda = it.nombreTipoDeuda
                    abonosRealizados = it.abonosRealizados
                    cuotasCanceladas = it.cuotasCanceladas
                    cuotasPendientes = it.cuotasPendientes
                }
            }

            val personaClienteData = RealmPersonaCliente().apply {
                id = data.empresaClienteContador.personaCliente.id
                codigo = data.empresaClienteContador.personaCliente.codigo
                direccion = direccionData
                deudaCliente = realmListOf<RealmDeudaCliente>().apply { addAll(deudaClienteList.orEmpty()) }
                discapacidad = data.empresaClienteContador.personaCliente.discapacidad
                numeroCedula = data.empresaClienteContador.personaCliente.numeroCedula
                primerNombre = data.empresaClienteContador.personaCliente.primerNombre
                segundoNombre = data.empresaClienteContador.personaCliente.segundoNombre
                primerApellido = data.empresaClienteContador.personaCliente.primerApellido
                segundoApellido = data.empresaClienteContador.personaCliente.segundoApellido
            }

            val ultimaFacturaData = RealmUltimaFactura().apply {
                fecha = data.empresaClienteContador.ultimaFactura?.fecha
                codigo = data.empresaClienteContador.ultimaFactura?.codigo
                precio = data.empresaClienteContador.ultimaFactura?.precio
                lectura = data.empresaClienteContador.ultimaFactura?.lectura
            }

            val employeeRoute = RealmEmployeeRoute().apply {
                id = data.empresaClienteContador.id
                this.personId = personId
                codFactura = data.empresaClienteContador.codFactura
                codConvenio = data.empresaClienteContador.codConvenio
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
        personId: Int,
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
                            rangoConsumo = tarifasEmpresa.rangoConsumo?.let { rangoConsumo ->
                                RealmRangoConsumo().apply {
                                    consuBasico = rangoConsumo.consuBasico
                                    consuSuntuario = rangoConsumo.consuSuntuario
                                    consuComplementario = rangoConsumo.consuComplementario
                                }
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
                this.personId = personId
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