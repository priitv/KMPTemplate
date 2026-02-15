package android.template.test.app.testdi

import android.template.feature.mymodel.business.contract.MyModelRepository
import android.template.feature.mymodel.di.DataModule
import android.template.feature.mymodel.di.FakeMyModelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface FakeDataModule {
    @Binds
    abstract fun bindRepository(
        fakeRepository: FakeMyModelRepository
    ): MyModelRepository
}
