import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class Order(val pizza: String, val size:String, val tip: String, val total: Double)

class PizzaViewModel : ViewModel() {
    var size by mutableStateOf("Moyenne")
    var tip by mutableStateOf("0%")
    var total by mutableStateOf(0.0)

    val history = mutableStateListOf<Order>()

    var pizza by mutableStateOf("Pepperoni")
    private val prices = mapOf("Petite" to 8.0, "Moyenne" to 12.0, "Grande" to 16.0)
    private val pizzaPrice = mapOf("Pepperoni" to 15.0, "Cheese" to 10.0, "Italian Sausage" to 17.0,
        "Suppreme(Everything)" to 25.0, "Mushroom" to 10.0)
    fun calculateTotal() {
        val pizza = pizzaPrice[pizza] ?: 0.0
        val base = prices[size] ?: 0.0
        val tipPercent = tip.removeSuffix("%").toDoubleOrNull() ?: 0.0
        total = pizza + base + base * (tipPercent / 100)
    }

    fun SaveOrder(){
        history.add(Order(pizza,size,tip,total))
    }
}