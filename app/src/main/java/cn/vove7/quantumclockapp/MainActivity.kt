package cn.vove7.quantumclockapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.vove7.quantumclock.QuantumClock
import cn.vove7.quantumclock.Syncher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startTimeLoop()
        log("当前时间： ${System.currentTimeMillis()}")

        //优先级测试
        QuantumClock.addSyncer(object : Syncher {
            override val name: String get() = "Local"

            override val priority: Int = 9
            var i = 0

            override suspend fun getMillisTime(): Long {
                if (i++ > 3) {
                    throw Exception("use taobao")
                }
                return 0
            }
        })
    }

    fun log(m: String) {
        log_text_view.append(m + "\n")
    }

    private var loopJob: Job? = null

    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private fun startTimeLoop() {
        loopJob = GlobalScope.launch {
            while (this.isActive) {
                runOnUiThread {
                    val diff = System.currentTimeMillis() - QuantumClock.currentTimeMillis
                    clock_view.text = sf.format(QuantumClock.nowDate) + "\ndiff with system: $diff"
                }
                delay(500)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loopJob?.cancel()
    }

    fun syncTime(view: View) {
        QuantumClock.sync().invokeOnCompletion { e ->
            log("同步完成 err: $e")
            log("当前时间： ${QuantumClock.currentTimeMillis}")
        }
    }
}