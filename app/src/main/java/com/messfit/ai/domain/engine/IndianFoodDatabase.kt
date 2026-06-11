package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.FoodItem
import com.messfit.ai.data.model.MacroNutrients
import com.messfit.ai.data.model.MicroNutrients

object IndianFoodDatabase {

    private val foods = mapOf(
        "rice" to FoodItem("Rice", MacroNutrients(206, 4f, 45f, 0.4f, 0.6f), MicroNutrients(0.8f, 16f, 0f, 0f, 0.8f)),
        "dal" to FoodItem("Dal", MacroNutrients(180, 12f, 28f, 2f, 8f), MicroNutrients(3.3f, 40f, 0f, 0f, 1.5f)),
        "roti" to FoodItem("Roti", MacroNutrients(120, 3.5f, 22f, 2.5f, 3f), MicroNutrients(1.2f, 20f, 0f, 0f, 0.9f)),
        "paneer" to FoodItem("Paneer", MacroNutrients(265, 18f, 4f, 20f, 0f), MicroNutrients(0.2f, 200f, 0f, 0.4f, 0.6f)),
        "rajma" to FoodItem("Rajma", MacroNutrients(210, 14f, 32f, 3f, 10f), MicroNutrients(2.9f, 60f, 0f, 0f, 1.2f)),
        "chole" to FoodItem("Chole", MacroNutrients(220, 11f, 35f, 4f, 9f), MicroNutrients(2.9f, 50f, 0f, 0f, 1.5f)),
        "soybean" to FoodItem("Soybean", MacroNutrients(170, 16f, 10f, 9f, 6f), MicroNutrients(5.1f, 102f, 0f, 0f, 1.5f)),
        "soya chunks" to FoodItem("Soya Chunks", MacroNutrients(120, 52f, 8f, 0.5f, 4f), MicroNutrients(3f, 80f, 0f, 0f, 1.2f), costInr = 12),
        "milk" to FoodItem("Milk", MacroNutrients(150, 8f, 12f, 8f, 0f), MicroNutrients(0.1f, 300f, 2.5f, 0.9f, 0.9f), costInr = 60),
        "curd" to FoodItem("Curd", MacroNutrients(98, 11f, 4f, 4f, 0f), MicroNutrients(0.1f, 150f, 0f, 0.4f, 0.6f), costInr = 25),
        "sprouts" to FoodItem("Sprouts", MacroNutrients(120, 9f, 18f, 1f, 5f), MicroNutrients(1.5f, 30f, 0f, 0f, 0.8f)),
        "egg" to FoodItem("Egg", MacroNutrients(78, 6f, 0.6f, 5f, 0f), MicroNutrients(0.9f, 28f, 1.1f, 0.6f, 0.6f), costInr = 7),
        "chicken" to FoodItem("Chicken", MacroNutrients(165, 31f, 0f, 3.6f, 0f), MicroNutrients(0.9f, 15f, 0.1f, 0.3f, 1.0f)),
        "fish" to FoodItem("Fish", MacroNutrients(140, 22f, 0f, 5f, 0f), MicroNutrients(0.5f, 20f, 5f, 2f, 0.5f)),
        "upma" to FoodItem("Upma", MacroNutrients(250, 6f, 40f, 8f, 3f)),
        "poha" to FoodItem("Poha", MacroNutrients(250, 5f, 45f, 6f, 2f)),
        "idli" to FoodItem("Idli", MacroNutrients(80, 3f, 16f, 0.5f, 1f)),
        "dosa" to FoodItem("Dosa", MacroNutrients(168, 4f, 28f, 4f, 1.5f)),
        "khichdi" to FoodItem("Khichdi", MacroNutrients(280, 10f, 48f, 5f, 4f)),
        "pulao" to FoodItem("Pulao", MacroNutrients(320, 8f, 52f, 9f, 2f)),
        "biryani" to FoodItem("Biryani", MacroNutrients(400, 15f, 55f, 14f, 2f)),
        "roasted chana" to FoodItem("Roasted Chana", MacroNutrients(180, 9f, 28f, 3f, 8f), costInr = 90),
        "peanuts" to FoodItem("Peanuts", MacroNutrients(160, 7f, 5f, 14f, 2.5f), costInr = 40),
        "sattu" to FoodItem("Sattu", MacroNutrients(140, 12f, 20f, 2f, 5f), costInr = 50),
        "tofu" to FoodItem("Tofu", MacroNutrients(144, 15f, 3f, 9f, 1f), costInr = 80),
        "tea" to FoodItem("Tea", MacroNutrients(30, 0f, 5f, 1f, 0f)),
        "paratha" to FoodItem("Paratha", MacroNutrients(260, 6f, 36f, 10f, 2f)),
        "sabzi" to FoodItem("Sabzi", MacroNutrients(120, 3f, 15f, 6f, 4f)),
        "salad" to FoodItem("Salad", MacroNutrients(50, 2f, 8f, 1f, 3f))
    )

    fun findFood(name: String): FoodItem? {
        val normalized = name.lowercase().trim()
        return foods.entries.firstOrNull { normalized.contains(it.key) }?.value
    }

    fun allFoodNames(): List<String> = foods.keys.toList()

    fun cheapProteinSources(isVegetarian: Boolean): List<FoodItem> {
        val sources = listOf(
            foods["soya chunks"]!!,
            foods["roasted chana"]!!,
            foods["milk"]!!,
            foods["curd"]!!,
            foods["paneer"]!!,
            foods["sattu"]!!,
            foods["peanuts"]!!,
            foods["tofu"]!!
        )
        return if (isVegetarian) sources else sources + listOf(foods["egg"]!!, foods["chicken"]!!)
    }

    fun estimateMealMacros(items: List<String>): MacroNutrients {
        var cal = 0
        var protein = 0f
        var carbs = 0f
        var fat = 0f
        var fiber = 0f
        items.forEach { item ->
            val food = findFood(item) ?: FoodItem(item, MacroNutrients(150, 5f, 20f, 5f, 2f))
            cal += food.macros.calories
            protein += food.macros.proteinG
            carbs += food.macros.carbsG
            fat += food.macros.fatG
            fiber += food.macros.fiberG
        }
        return MacroNutrients(cal, protein, carbs, fat, fiber)
    }
}
