package cn.vove7.quantumclockapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.vove7.quantumclock.QuantumClock
import cn.vove7.quantumclock.Syncher
import cn.vove7.quantumclockapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startTimeLoop()
        log("当前时间： ${System.currentTimeMillis()}")
    }

    fun log(m: String) {
        binding.logTextView.append(m + "\n")
    }

    private var loopJob: Job? = null

    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private fun startTimeLoop() {
        loopJob = GlobalScope.launch {
            while (this.isActive) {
                runOnUiThread {
                    val diff = System.currentTimeMillis() - QuantumClock.currentTimeMillis
                    binding.clockView.text = sf.format(QuantumClock.nowDate) + "\ndiff with system: $diff"
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