import de.dhorn.dotacrawler.network.DotaApi
import de.dhorn.dotacrawler.network.SteamApi
import de.dhorn.dotacrawler.storage.Storage
import java.util.*
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

fun main(args: Array<String>) {
    // Instantiate library
    val dotaApi = DotaApi(getSteamApiToken())
    val storage = Storage.localPostgres()

    // Find initial sequence number through search algorithm
    // TODO: if we have records in the DB that are not older than 2 weeks, use newest MSN
    val firstMSN = dotaApi.firstMSNAtMost2WeeksAgo()

    // Starting at last crawled sequence number, continuously request 100 matches every 3 seconds until we catch up;
    // Afterwards, 2 request per minute should suffice (12000 matches / hour seems realistic)
    //
    // NOTE: 1 request every 3 seconds
    //   => 20 requests per minute
    //   => 1200 requests per hour
    //   => 120 000 matches crawled per hour (~1 day of dota matches per hour, i.e., 24x realtime speed)

    // TODO: (other microservice/app/general)
    //  - implement queries + set indices on DB to optimize
    //  - build REST endpoints as separate kotlin (?) microservice with nice documentation (Swagger)
    //  - intelligently set indices on DB to optimize said queries
    //  - nice documentation README for project
    //  - deploy to google cloud or equivalent (stay as platform independent as possible!)
    //  - crawl additional data (heroes, items etc), build endpoints for those and use them in the app
}