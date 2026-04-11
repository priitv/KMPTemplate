package android.template.feature.mymodel.di

import android.template.feature.mymodel.business.AddMyModelUseCase
import android.template.feature.mymodel.business.AddMyModelUseCaseImpl
import android.template.feature.mymodel.business.GetMyModelsUseCase
import android.template.feature.mymodel.business.GetMyModelsUseCaseImpl
import android.template.feature.mymodel.business.contract.MyModelRepository
import android.template.feature.mymodel.data.MyModelRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val myModelModule: Module = module {
    single<MyModelRepository> { MyModelRepositoryImpl(get()) }
    factory<GetMyModelsUseCase> { GetMyModelsUseCaseImpl(get()) }
    factory<AddMyModelUseCase> { AddMyModelUseCaseImpl(get()) }
}
