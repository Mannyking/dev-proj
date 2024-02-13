package com.example.mobilebanking

import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

var jsonValue : String = ""
fun main() {
    val githubUserName = "Mannyking" // Replace 'username' with the GitHub username you want to fetch

    val client = OkHttpClient()

    // Use a coroutine to make a network request
    val job = CoroutineScope(Dispatchers.IO).launch {
        val request = Request.Builder()
            .url("https://api.github.com/users/$githubUserName")
            .build()

        val response = client.newCall(request).execute()

        // Check if the response is successful and process data
        if (response.isSuccessful) {
            val responseData = response.body?.string()
            println("User Details: $responseData")
            // Process and use the fetched data here
        } else {
            println("Request failed: ${response.code}")
        }
    }

    // Optionally, delay or do other tasks before cancelling the coroutine
//    runBlocking {
//        delay(5000) // Delay for 5 seconds
//        // Cancel the coroutine after a delay
//        job.cancel()
//    }
}

fun secondRequest() {
    val githubUserName = "Abel" // Replace 'username' with the GitHub username you want to fetch

    val client = OkHttpClient()

    // Use a coroutine to make a network request
    val job = CoroutineScope(Dispatchers.IO).launch {
        val request = Request.Builder()
            .url("https://api.github.com/users/$githubUserName")
            .build()

        val response = client.newCall(request).execute()

        // Check if the response is successful and process data
        if (response.isSuccessful) {
            val responseData = response.body?.string()
            val jsonObject = JSONObject(responseData)
            jsonValue = jsonObject.getString("login")
            println("User Details: $responseData")
            // Process and use the fetched data here
        } else {
            println("Request failed: ${response.code}")
        }
    }
    println("Hello $jsonValue")
}
