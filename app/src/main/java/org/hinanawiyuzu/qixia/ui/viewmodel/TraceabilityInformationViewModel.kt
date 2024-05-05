package org.hinanawiyuzu.qixia.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.hinanawiyuzu.qixia.data.entity.Traceability
import org.hinanawiyuzu.qixia.data.source.fake.fakeTraceabilityInformation
import org.hinanawiyuzu.qixia.ui.route.BoxRoute
import org.hinanawiyuzu.qixia.ui.route.RemindRoute
import org.hinanawiyuzu.qixia.ui.viewmodel.shared.SharedTraceabilityViewModel

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
    isFromBox: Boolean,
    sharedViewModel: SharedTraceabilityViewModel
  ) {
    traceability?.let {
      sharedViewModel.traceability = traceability
      sharedViewModel.isNeedAdd = true
    }
    Log.e("isFromBox", isFromBox.toString())
    if (isFromBox) {
      navController.navigate(route = BoxRoute.NewMedicineScreen.name) {
        popUpTo(route = BoxRoute.BoxScreen.name) {
          inclusive = false
        }
      }
    } else {
      navController.popBackStack()
    }
  }
}