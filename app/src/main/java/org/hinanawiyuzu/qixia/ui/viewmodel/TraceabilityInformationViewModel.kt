package org.hinanawiyuzu.qixia.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.navigation.*
import org.hinanawiyuzu.qixia.data.entity.*
import org.hinanawiyuzu.qixia.data.source.fake.*
import org.hinanawiyuzu.qixia.ui.route.*
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.*

class TraceabilityInformationViewModel : ViewModel() {
    var code: String? by mutableStateOf(null)
    var traceability: Traceability? by mutableStateOf(null)
    fun searchTraceability() {
        code?.let {
            traceability = fakeTraceabilityInformation.find { it.traceabilityCode == code }
        }
    }

    fun onCheckDetailButtonClicked(
        navController: NavController,
        sharedViewModel: SharedTraceabilityViewModel
    ) {
        sharedViewModel.traceability = traceability
        navController.navigate(RemindRoute.TraceabilityDetailScreen.name)
    }

    fun onAddToMedicineClicked(
        navController: NavController,
        sharedViewModel: SharedTraceabilityViewModel
    ) {
        traceability?.let {
            sharedViewModel.traceability = traceability
            sharedViewModel.isNeedAdd = true
        }
        navController.navigate(RemindRoute.NewMedicineScreen.name) {
            popUpTo(RemindRoute.NewMedicineScreen.name) {
                inclusive = true
            }
        }
    }
}