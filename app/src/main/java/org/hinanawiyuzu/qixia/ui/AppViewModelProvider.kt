package org.hinanawiyuzu.qixia.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.ui.viewmodel.*

/**
 * 工厂-用于创建App的所有ViewModel。
 */
object AppViewModelProvider {
  val factory = viewModelFactory {
    initializer {
      val application = qixiaApplication()
      LoginViewModel(application.container.userRepository)
    }
    initializer {
      val application = qixiaApplication()
      FillPersonalInformationViewModel(
        userRepository = application.container.userRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      WelcomeViewModel(
        application = application,
        userRepository = application.container.userRepository,
      )
    }
    initializer {
      val application = qixiaApplication()
      NewRemindViewModel(
        medicineRemindRepository = application.container.medicineRemindRepository,
        medicineRepoRepository = application.container.medicineRepoRepository,
        alarmDateTimeRepository = application.container.alarmDateTimeRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      RemindViewModel(
        medicineRemindRepository = application.container.medicineRemindRepository,
        medicineRepoRepository = application.container.medicineRepoRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      MedicineRepoViewModel(
        medicineRepoRepository = application.container.medicineRepoRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      MedicineRepoListViewModel(
        medicineRepoRepository = application.container.medicineRepoRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      NewMedicineViewModel(
        medicineInfoRepository = application.container.medicineInfoRepository,
        medicineRepoRepository = application.container.medicineRepoRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      MainViewModel(
        userRepository = application.container.userRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      BoxViewModel(
        userRepository = application.container.userRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      RecordViewModel(
        medicineRemindRepository = application.container.medicineRemindRepository,
        application = application
      )
    }
    initializer {
      val application = qixiaApplication()
      ProfileViewModel(
        userRepository = application.container.userRepository,
        application = application
      )
    }
  }
}

/**
 * 在 ViewModelProvider.Factory.create 中传递的简单类似映射对象，用于向工厂提供额外信息。
 * 它允许使工厂实现无状态，这样更容易注入工厂，因为不需要在构造时提供所有信息。
 */
fun CreationExtras.qixiaApplication(): QixiaApplication =
  (this[AndroidViewModelFactory.APPLICATION_KEY] as QixiaApplication)