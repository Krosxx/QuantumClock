package cn.vove7.quantumclock.synchers


import cn.vove7.quantumclock.Syncher
import cn.vove7.quantumclock.util.httpGet
import org.json.JSONObject
import java.util.Calendar
import java.util.TimeZone

object TimeApiSyncher : Syncher {
    override val priority: Int = 100

    override val name: String get() = "TimeApi2"

    override suspend fun getMillisTime(): Long {
        val data = httpGet("https://timeapi.io/api/Time/current/zone?timeZone=UTC")
        val obj = JSONObject(data)
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.YEAR, obj.getInt("year"))
            set(Calendar.MONTH, obj.getInt("month") - 1)
            set(Calendar.DAY_OF_MONTH, obj.getInt("day"))
            set(Calendar.HOUR_OF_DAY, obj.getInt("hour"))
            set(Calendar.MINUTE, obj.getInt("minute"))
            set(Calendar.SECOND, obj.getInt("seconds"))
            set(Calendar.MILLISECOND, obj.getInt("milliSeconds"))
        }.timeInMillis
    }
}
