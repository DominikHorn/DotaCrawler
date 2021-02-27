package de.dhorn.dotacrawler.network

import com.beust.klaxon.Klaxon
import de.dhorn.dotacrawler.network.types.Match
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.system.exitProcess

class DotaApi(token: String) : SteamApi(token) {
    // We may only make one api call every 1 seconds
    override val requestThrottle: Long = 1000

    private fun searchRangeBounds(timestamp: Long, startSequenceNumber: Long): Pair<Long, Long> {
        var sequenceNumber = startSequenceNumber

        while (true) {
            // If match == null, we exceeded the valid search range, i.e., if our result exists it must lie within
            // [sequenceNumber/2, sequenceNumber)
            val match = getMatchBySequenceNumber(sequenceNumber) ?: return Pair(sequenceNumber / 2, sequenceNumber)

            if (match.start_time > timestamp) {
                // TODO: this only truely works if startSequenceNumber does not
                //  already satisfy match.time > timestamp
                return Pair(match.sequence_number / 2, match.sequence_number)
            }

            // Exponential increase
            sequenceNumber *= 2
        }

    }

    /**
     * Returns the first match with a sequence number at least as big as
     * the given sequence number (sometimes there are gaps in the MSNs)
     */
    fun getMatchBySequenceNumber(sequenceNumber: Long): Match? {
        val params = mapOf(Pair("start_at_match_seq_num", "$sequenceNumber"), Pair("matches_requested", "1"))
        val response = request(Route.GET_MATCH_HISTORY_BY_SEQUENCE_NUM.endpoint, params)
        return Klaxon().parse<MatchResponse>(response.body())?.result?.matches?.firstOrNull()
    }

    /**
     * Returns a match sequence number of the first match played at most 2 weeks ago
     */
    fun firstMSNAtMost2WeeksAgo(): Long? {
        val twoWeeksAgo = (Date().time / 1000) - 14 * 24 * 60 * 60

        // Obtain bounds where sequence number can lay through exponential search
        val bounds = searchRangeBounds(timestamp = twoWeeksAgo, startSequenceNumber = (2.0).pow(32).toLong())
        var lowerBound = bounds.first
        var upperBound = bounds.second

        // Binary search interval
        var match: Match?
        var bestMatch: Match? = null
        while (upperBound - lowerBound > 1) {
            val interval = (upperBound - lowerBound)
            val testMSN = lowerBound + interval / 2
            match = getMatchBySequenceNumber(testMSN)

            // Update current best match fitting searched value
            if (match != null && (bestMatch == null || bestMatch.start_time > match.start_time) && match.start_time >= twoWeeksAgo) {
                bestMatch = match
            }

            if (match == null || match.start_time > twoWeeksAgo) {
                // Continue searching to the left
                upperBound -= interval / 2
            } else {
                // Continue searching to the right
                lowerBound += interval / 2
            }
        }

        return bestMatch?.sequence_number
    }

    enum class Route(val endpoint: String) {
        GET_MATCH_HISTORY_BY_SEQUENCE_NUM("IDOTA2Match_570/GetMatchHistoryBySequenceNum/v1")
    }

    class MatchResponse(val result: MatchResponseResult)
    class MatchResponseResult(
        // 1 means everything is okay (?)
        val status: Int,
        val matches: List<Match>
    )
}