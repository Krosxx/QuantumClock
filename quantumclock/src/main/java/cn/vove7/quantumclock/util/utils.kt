package cn.vove7.quantumclock.util

import java.net.URL

/**
 * # utils
 *
 * Created on 2020/4/30
 * @author Vove
 */

//simple http get
fun httpGet(url: String): String {
    val con = URL(url).openConnection()
    con.connectTimeout = 5000
    con.connect()
    return con.getInputStream().bufferedReader().readText()
}