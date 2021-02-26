package de.dhorn.dotacrawler.network

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class SteamApi(val token: String) {
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

    fun request(route: Route, parameters: Map<String, String>): HttpResponse<String> {
        val defaultParameters = mapOf(Pair("key", token), Pair("format", "json"), Pair("language", "en"))
        val encodedParams = encodeParameters(defaultParameters + parameters)

        val url = "http://api.steampowered.com/${route.endpoint}?$encodedParams"
        print("URL: $url")

        val request = HttpRequest
            .newBuilder()
            .GET()
            .uri(URI.create(url))
            .build()

//        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    enum class Route(val endpoint: String) {
        GET_MATCH_HISTORY_BY_SEQUENCE_NUM("IDOTA2Match_570/GetMatchHistoryBySequenceNum/v1")
    }
}