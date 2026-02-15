package android.template.feature.mymodel.di

import android.template.feature.mymodel.business.AddMyModel
import android.template.feature.mymodel.business.AddMyModelImpl
import android.template.feature.mymodel.business.GetMyModels
import android.template.feature.mymodel.business.GetMyModelsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BusinessModule {
    @Singleton
    @Binds
    fun bindsAddMyModel(
        addMyModel: AddMyModelImpl
    ): AddMyModel

    @Singleton
    @Binds
    fun bindsGetMyModels(
        getMyModels: GetMyModelsImpl
    ): GetMyModels
}