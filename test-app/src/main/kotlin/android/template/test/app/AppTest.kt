package android.template.test.app

import android.template.app.MainActivity
import android.template.feature.mymodel.business.contract.MyModelRepository
import android.template.shared.app.appModules
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

class AppTest : KoinTest {
    private val mockRepo = mockk<MyModelRepository> {
        every { myModels } returns flowOf(listOf("One", "Two", "Three"))
    }

    @get:Rule(order = 1)
    val koinTestRule = KoinTestRule.create {
        androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        modules(appModules + listOf(module { single<MyModelRepository> { mockRepo } }))
    }

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun whenAppLaunches_thenItemsAreDisplayed() {
        composeTestRule.onNodeWithText("One", substring = true).assertExists()
    }
}
