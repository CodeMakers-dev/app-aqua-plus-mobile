package com.codemakers.aquaplus.domain.models


data class Profile(
    val id: Int,
    val role: Role,
    val person: Person,
    val username: String,
    val email: String?,
    val passwordHash: String,
    val imageBase64: String?,
    val isActive: Boolean,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)


data class Role(
    val id: Int,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)


data class Person(
    val id: Int,
    val address: Address,
    val documentType: DocumentType,
    val idNumber: String,
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val secondLastName: String?,
    val code: String?,
    val description: String?,
    val isActive: Boolean,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)


data class Address(
    val id: Int,
    val department: Department?,
    val city: City?,
    val district: District?,
    val description: String,
    val isActive: Boolean,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)


data class Department(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)


data class City(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val departmentDetails: Department?,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)


data class District(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val cityDetails: City?,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)


data class DocumentType(
    val id: Int,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val createdBy: String?,
    val createdAt: String?,
    val modifiedBy: String?,
    val modifiedAt: String?
)