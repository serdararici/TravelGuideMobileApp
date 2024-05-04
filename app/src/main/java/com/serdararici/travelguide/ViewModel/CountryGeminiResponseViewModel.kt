package com.serdararici.travelguide.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.serdararici.travelguide.BuildConfig
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CountryGeminiResponseViewModel : ViewModel() {

    private val _responseText = MutableLiveData<String>()
    private val apiKey = BuildConfig.API_KEY_GEMINI
    val responseText: LiveData<String> get() = _responseText

    init {

    }

    fun generateContent(countryName: String, category: String, prompt:String) {

        viewModelScope.launch {
            try {
                val generativeModel = GenerativeModel(
                    modelName = "gemini-pro",
                    apiKey = apiKey
                )

                if (isActive) {  // Coroutine'in hala aktif olduğunu kontrol et
                    val response = generativeModel.generateContent(prompt)
                    _responseText.value = response.text
                }

            } catch (e: CancellationException) {
                println("Coroutine was cancelled")
            } catch (e: Exception) {
                println("An error occurred: ${e.message}")
            }
        }

        /*viewModelScope.launch {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = apiKey
            )
            val response = generativeModel.generateContent(prompt)
            _responseText.value = response.text
        }*/
    }
}