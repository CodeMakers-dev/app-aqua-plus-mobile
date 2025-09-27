package com.codemakers.aquaplus.data.models.response

import com.codemakers.aquaplus.domain.models.Address
import com.codemakers.aquaplus.domain.models.City
import com.codemakers.aquaplus.domain.models.Department
import com.codemakers.aquaplus.domain.models.District
import com.codemakers.aquaplus.domain.models.DocumentType
import com.codemakers.aquaplus.domain.models.Person
import com.codemakers.aquaplus.domain.models.Profile
import com.codemakers.aquaplus.domain.models.Role
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("rol")
    val role: RoleDto,

    @SerializedName("persona")
    val person: PersonDto,

    @SerializedName("nombre")
    val username: String,

    @SerializedName("correo")
    val email: String?,

    @SerializedName("contrasena")
    val passwordHash: String,

    @SerializedName("imagen")
    val imageBase64: String?,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

@Serializable
data class RoleDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("descripcion")
    val description: String?,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

@Serializable
data class PersonDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("direccion")
    val address: AddressDto,

    @SerializedName("tipoDocumento")
    val documentType: DocumentTypeDto,

    @SerializedName("numeroCedula")
    val idNumber: String,

    @SerializedName("nombre")
    val firstName: String,

    @SerializedName("segundoNombre")
    val middleName: String?,

    @SerializedName("apellido")
    val lastName: String,

    @SerializedName("segundoApellido")
    val secondLastName: String?,

    @SerializedName("codigo")
    val code: String?,

    @SerializedName("descripcion")
    val description: String?,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

@Serializable
data class AddressDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("departamentoId")
    val department: DepartmentDto?,

    @SerializedName("ciudadId")
    val city: CityDto?,

    @SerializedName("corregimientoId")
    val district: DistrictDto?,

    @SerializedName("descripcion")
    val description: String,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

@Serializable
data class DepartmentDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

@Serializable
data class CityDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("departamento")
    val departmentDetails: DepartmentDto?,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

@Serializable
data class DistrictDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("ciudad")
    val cityDetails: CityDto?,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

@Serializable
data class DocumentTypeDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("descripcion")
    val description: String?,

    @SerializedName("activo")
    val isActive: Boolean,

    @SerializedName("usuarioCreacion")
    val createdBy: String?,

    @SerializedName("fechaCreacion")
    val createdAt: String?,

    @SerializedName("usuarioModificacion")
    val modifiedBy: String?,

    @SerializedName("fechaModificacion")
    val modifiedAt: String?
)

fun ProfileDto.toDomain() = Profile(
    id = id,
    role = role.toDomain(),
    person = person.toDomain(),
    username = username,
    email = email,
    passwordHash = passwordHash,
    imageBase64 = imageBase64,
    isActive = isActive,
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)

fun RoleDto.toDomain() = Role(
    id = id,
    name = name,
    description = description,
    isActive = isActive,
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)

fun PersonDto.toDomain() = Person(
    id = id,
    address = address.toDomain(),
    documentType = documentType.toDomain(),
    idNumber = idNumber,
    firstName = firstName,
    middleName = middleName,
    lastName = lastName,
    secondLastName = secondLastName,
    code = code,
    description = description,
    isActive = isActive,
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)

fun AddressDto.toDomain() = Address(
    id = id,
    department = department?.toDomain(),
    city = city?.toDomain(),
    district = district?.toDomain(),
    description = description,
    isActive = isActive,
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)

fun DepartmentDto.toDomain() = Department(
    id = id,
    name = name,
    isActive = isActive,
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)

fun CityDto.toDomain() = City(
    id = id,
    name = name,
    isActive = isActive,
    departmentDetails = departmentDetails?.toDomain(),
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)

fun DistrictDto.toDomain() = District(
    id = id,
    name = name,
    isActive = isActive,
    cityDetails = cityDetails?.toDomain(),
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)

fun DocumentTypeDto.toDomain() = DocumentType(
    id = id,
    name = name,
    description = description,
    isActive = isActive,
    createdBy = createdBy,
    createdAt = createdAt,
    modifiedBy = modifiedBy,
    modifiedAt = modifiedAt
)