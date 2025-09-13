package tiptime

import com.example.tipcalculator.calculateTip
import org.junit.Test
import java.text.NumberFormat
import org.junit.Assert.assertEquals

class TipCalculatorTests {
    @Test
    fun calculateTip_20PercentNoRoundUp() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectTip = NumberFormat.getCurrencyInstance().format(2)
        val actual = calculateTip(amount = amount, tipPercent = tipPercent, false)
        assertEquals(expectTip, actual)
    }
}
