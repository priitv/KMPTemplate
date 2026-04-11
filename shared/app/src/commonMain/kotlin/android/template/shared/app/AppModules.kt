package android.template.shared.app

import android.template.core.database.databaseModule
import android.template.feature.mymodel.di.myModelAppModule
import android.template.feature.mymodel.di.myModelModule
import org.koin.core.module.Module

val appModules: List<Module> = listOf(databaseModule, myModelModule, myModelAppModule)
