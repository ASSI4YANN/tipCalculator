package tiptime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import java.text.NumberFormat
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.tipcalculator.TipTimeLayout
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tipcalculator.R
import org.junit.Rule
import org.junit.Test

class TipUITests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            TipCalculatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("10")
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val expectedTipText = context.getString(R.string.tip_amount, expectedTip)
        composeTestRule.onNodeWithText(expectedTipText)
            .assertExists("No node with this text was found.")
    }
}
