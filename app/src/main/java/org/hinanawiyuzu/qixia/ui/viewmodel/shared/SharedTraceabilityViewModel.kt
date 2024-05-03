package org.hinanawiyuzu.qixia.ui.viewmodel.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.hinanawiyuzu.qixia.data.entity.Traceability

class SharedTraceabilityViewModel : ViewModel() {
  /**
   * 产品追溯信息
   */
  var traceability: Traceability? by mutableStateOf(null)

  /**
   * 用户是否需要把扫描到的药品添加到仓库中
   */
  var isNeedAdd: Boolean by mutableStateOf(false)
}