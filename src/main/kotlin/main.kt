import de.dhorn.dotacrawler.network.SteamApi
import de.dhorn.dotacrawler.storage.Storage
import kotlin.system.exitProcess

fun getSteamApiToken(): String {
    val tokenKey = "STEAM_DEV_TOKEN"

    val token = System.getenv(tokenKey)
    if (token.isNullOrEmpty()) {
        System.err.println("Please provide a steam development token via the $tokenKey environment variable")
        exitProcess(-1)
    }

    return token
}

// Algorithm Idea:
// 1. Find first match sequence number we want (timestamp >= oneWeekAgo)
//      1.1. If DB is empty or last match is to long ago, use through exponential search (?) & request 1 match each
//      1.2. Else continue from last crawled sequence number
// 2. Starting at last crawled sequence number, continuously request 100 matches every 3 seconds until we catch up
//      2.1. Decode to JSON
//      2.2. M2M to DB Schema
//      2.3. Store in DB
//
//      NOTE: 1 request every 3 seconds
//        => 20 requests per minute
//        => 1200 requests per hour
//        => 120 000 matches crawled per hour (~1 day of dota matches per hour, i.e., 24x realtime speed)
// 3. Dynamically throttle requests as is fit i.e., adjust sleep between crawls based on how caught up we are.
//    => 50 requests per hour should suffice, i.e., one request per minute tops during normal operations
fun main(args: Array<String>) {
    // Instantiate library
    val steam = SteamApi(getSteamApiToken())
    val storage = Storage.localPostgres()

    // Initialize storage
    storage.create()

    // TODO: find initial sequence number through search algorithm

    val params = mapOf(Pair("start_at_match_seq_num", "5000000"), Pair("matches_requested", "10"))
    val response = steam.request(SteamApi.Route.GET_MATCH_HISTORY_BY_SEQUENCE_NUM, params)
    print(response.body())




    // Further TODO:
    // - implement queries + REST endpoints as separate kotlin (?) server
    // - intelligently set indices on DB to optimize said queries
    // - crawl additional data (heroes, items etc) and serve them through dedicated endpoints
    // - nice documentation README/Swagger API etc
    // - deploy to google cloud or equivalent (stay as platform independent as possible!)
    // - switch app to using these dedicated endpoints
}