import de.dhorn.dotacrawler.network.SteamApi
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val tokenKey = "STEAM_DEV_TOKEN"
    val token = System.getenv(tokenKey)
    if (token.isNullOrEmpty()) {
        System.err.println("Please provide a steam development token via the $tokenKey environment variable")
        exitProcess(-1)
    }

    val steam = SteamApi(token)

    val params = mapOf(Pair("start_at_match_seq_num", "5000000"), Pair("matches_requested", "10"))
    steam.request(SteamApi.Route.GET_MATCH_HISTORY_BY_SEQUENCE_NUM, params).thenAccept {
        print(it.body())
    }
}