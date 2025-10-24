package com.codemakers.aquaplus.ui.modules.home.features.form

import com.codemakers.aquaplus.domain.models.EmployeeRoute
import com.codemakers.aquaplus.domain.models.EmployeeRouteConfig
import com.codemakers.aquaplus.domain.models.ReadingFormData

data class ReadingFormUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCreatedOrUpdatedSuccess: Boolean = false,

    val serial: String = "",
    val meterReading: String = "",
    val abnormalConsumption: Boolean? = null,
    val observations: String = "",

    val employeeRouteId: Int = -1,
    val route: EmployeeRoute? = null,
    val config: EmployeeRouteConfig? = null,
    val readingFormData: ReadingFormData? = null,
) {
    val readingFormDataId: Long?
        get() = readingFormData?.id

    val hasExistingReading: Boolean
        get() = readingFormData?.meterReading?.isNotBlank() == true

    val enableSave: Boolean
        get() = serial.isNotBlank() &&
                meterReading.isNotBlank() &&
                (readingFormData?.abnormalConsumption != abnormalConsumption ||
                        readingFormData?.meterReading != meterReading ||
                        readingFormData.observations != observations)

}