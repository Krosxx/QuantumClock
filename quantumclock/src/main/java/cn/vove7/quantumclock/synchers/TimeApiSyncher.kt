package cn.vove7.quantumclock.synchers


import cn.vove7.quantumclock.Syncher
import cn.vove7.quantumclock.util.httpGet
import org.json.JSONObject

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeApiSyncher : Syncher {
    override val priority: Int = -1

    override val name: String get() = "TimeApi"

    override suspend fun getMillisTime(): Long {
        val data = httpGet("https://timeapi.io/api/Time/current/zone?timeZone=UTC")
        val obj = JSONObject(data)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
        val time = LocalDateTime.parse(obj.getString("dateTime"), formatter)
        return time.toInstant(ZoneOffset.UTC).toEpochMilli()
    }
}