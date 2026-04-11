package android.template.feature.mymodel.di

import android.template.feature.mymodel.app.MyModelViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val myModelAppModule: Module = module {
    viewModelOf(::MyModelViewModel)
}
