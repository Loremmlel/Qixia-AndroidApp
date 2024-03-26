package org.hinanawiyuzu.qixia.ui.state

data class FillPersonalInformationUiState(
    val maleSelected: Boolean = true,
    val femaleSelected: Boolean = false,
    val age: String = "",
    val serialNumber: String = ""
)