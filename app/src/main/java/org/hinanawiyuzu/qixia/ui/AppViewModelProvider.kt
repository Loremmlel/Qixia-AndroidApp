package org.hinanawiyuzu.qixia.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.hinanawiyuzu.qixia.QixiaApplication
import org.hinanawiyuzu.qixia.ui.viewmodel.FillPersonalInformationViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.LoginViewModel
import org.hinanawiyuzu.qixia.ui.viewmodel.WelcomeViewModel

/**
 * 工厂-用于创建App的所有ViewModel。
 */
object AppViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            LoginViewModel(qixiaApplication().container.userRepository)
        }
        initializer {
            FillPersonalInformationViewModel(qixiaApplication().container.userRepository)
        }
        initializer {
            WelcomeViewModel(
                application = qixiaApplication(),
                userRepository =  qixiaApplication().container.userRepository,
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