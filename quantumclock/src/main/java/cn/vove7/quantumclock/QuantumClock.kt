package cn.vove7.quantumclock

import android.os.SystemClock
import cn.vove7.quantumclock.synchers.TimeApiSyncher
import kotlinx.coroutines.*
import java.util.*
import kotlin.reflect.KClass

interface Syncher : Comparable<Syncher> {
    val name: String

    //优先级
    val priority: Int

    //不进行异常处理，仅返回成功结果
    //网络时间 忽略误差，有需求可自定义来处理误差
    suspend fun getMillisTime(): Long
    override fun compareTo(other: Syncher): Int = other.priority - this.priority
}

object QuantumClock {

    val currentTimeMillis: Long get() = syncedTime + SystemClock.elapsedRealtime() - lastSyncUpTime

    val nowDate: Date get() = Date(currentTimeMillis)

    val nowCalendar: Calendar
        get() = Calendar.getInstance().also {
            it.timeInMillis = currentTimeMillis
        }


    private var lastSyncUpTime = SystemClock.elapsedRealtime()

    private var syncedTime: Long = System.currentTimeMillis()

    var syncSuccessed = false
        private set

    var syncWithClass: KClass<out Syncher>? = null
        private set

    private val synchers = TreeSet<Syncher>().also {
        it.add(TimeApiSyncher)
    }

    fun addSyncer(syncer: Syncher) = synchers.add(syncer)
    fun removeSyncer(syncer: Syncher) = synchers.remove(syncer)

    private val errHandler = CoroutineExceptionHandler { _, _ -> }

    fun sync(): Job = GlobalScope.launch(errHandler + Dispatchers.IO, block = ::doSync)

    @Suppress("UNUSED_PARAMETER")
    private suspend fun doSync(scope: CoroutineScope) {
        val errs = mutableListOf<Throwable>()
        synchers.forEach { syncher ->
            kotlin.runCatching {
                syncher.getMillisTime()
            }.onSuccess { time ->
                lastSyncUpTime = SystemClock.elapsedRealtime()
                syncedTime = time
                syncSuccessed = true
                syncWithClass = syncher::class
                return
            }.onFailure { e ->
                e.printStackTrace()
                errs.add(e)
            }
        }
        throw SyncFailedException(errs)
    }

}

data class SyncFailedException(val errors: List<Throwable>) : Exception()
