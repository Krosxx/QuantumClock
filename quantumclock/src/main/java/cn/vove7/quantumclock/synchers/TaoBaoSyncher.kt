package cn.vove7.quantumclock.synchers

import cn.vove7.quantumclock.Syncher
import cn.vove7.quantumclock.util.httpGet
import org.json.JSONObject

/**
 * # TaoBaoSyncher
 *
 * Created on 2020/4/30
 * @author Vove
 */
object TaoBaoSyncher : Syncher {
    override val priority: Int = -1

    override val name: String get() = "淘宝"

    override suspend fun getMillisTime(): Long {
        val data = httpGet("http://api.m.taobao.com/rest/api3.do?api=mtop.common.getTimestamp")
        val obj = JSONObject(data)
        return (obj["data"] as JSONObject).getString("t").toLong()
    }
}