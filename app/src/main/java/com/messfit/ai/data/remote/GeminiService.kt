package com.messfit.ai.data.remote

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.messfit.ai.BuildConfig
import com.messfit.ai.domain.engine.MenuIntelligenceEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {

    private val model: GenerativeModel? by lazy {
        val key = BuildConfig.GEMINI_API_KEY
        if (key.isBlank()) null
        else GenerativeModel(modelName = "gemini-flash-latest", apiKey = key)
    }

    suspend fun extractMenuFromImage(bitmap: Bitmap): String {
        val gemini = model ?: return "[]"

        return try {
            val response = gemini.generateContent(
                content {
                    image(bitmap)
                    text(MenuIntelligenceEngine.buildGeminiPrompt())
                }
            )
            response.text?.let { extractJsonArray(it) } ?: "[]"
        } catch (_: Exception) {
            "[]"
        }
    }

    suspend fun extractMenuFromText(text: String): String {
        val gemini = model ?: return "[]"
        return try {
            val response = gemini.generateContent(
                "${MenuIntelligenceEngine.buildGeminiPrompt()}\n\nMenu text:\n$text"
            )
            response.text?.let { extractJsonArray(it) } ?: "[]"
        } catch (_: Exception) {
            "[]"
        }
    }

    private fun extractJsonArray(text: String): String {
        val start = text.indexOf('[')
        val end = text.lastIndexOf(']')
        return if (start >= 0 && end > start) text.substring(start, end + 1) else "[]"
    }
}
