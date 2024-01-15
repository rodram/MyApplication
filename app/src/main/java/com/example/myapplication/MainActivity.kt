package com.example.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import androidx.compose.ui.tooling.preview.Preview

data class ApiResponse(val fact: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val apiService = Retrofit.Builder()
                .baseUrl("https://catfact.ninja/") // Use your API base URL here
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)

            CounterScreen(apiService)
        }
    }
}

interface ApiService {
    @GET("fact")
    suspend fun fetchData(): ApiResponse
}

@Composable
fun CounterScreen(apiService: ApiService) {
    var count by remember { mutableStateOf(1) }
    var apiResponse by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Count: $count")
        Text(text = "API Response: $apiResponse")
        Button(onClick = {
            coroutineScope.launch {
                try {
                    val response = apiService.fetchData()
                    apiResponse = response.fact
                    count++
                } catch (e: Exception) {
                    apiResponse = "Error: ${e.message}"

                }
            }
        }) {
            Text("Get data")
        }
    }
}



