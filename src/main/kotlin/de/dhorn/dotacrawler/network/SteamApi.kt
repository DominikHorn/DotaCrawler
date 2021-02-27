package de.dhorn.dotacrawler.network

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

abstract class SteamApi(val token: String) {
    private val client = HttpClient
        .newBuilder()
        .build()

    private fun encodeParameters(params: Map<String, String>): String {
        var result = ""
        params.forEach {
            val encodedKey = java.net.URLEncoder.encode(it.key, "utf-8")
            val encodedValue = java.net.URLEncoder.encode(it.value, "utf-8")

            val param = "$encodedKey=${encodedValue}"
            result = if (result.isEmpty()) param else "$result&$param"
        }
        return result
    }

    // Minimum amount of milliseconds between requests
    abstract val requestThrottle: Long
    private var lastRequestTime: Long = 0

    fun request(route: String, parameters: Map<String, String>): HttpResponse<String> {
        val delta = Date().time - lastRequestTime
        if (delta < requestThrottle) {
            Thread.sleep(requestThrottle - delta)
        }
        lastRequestTime = Date().time

        val defaultParameters = mapOf(Pair("key", token), Pair("format", "json"), Pair("language", "en_us"))
        val encodedParams = encodeParameters(defaultParameters + parameters)

        val url = "http://api.steampowered.com/$route?$encodedParams"

        val request = HttpRequest
            .newBuilder()
            .GET()
            .uri(URI.create(url))
            .build()

        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}