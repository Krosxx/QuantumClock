# QuantumClock

> 你是否为 App 内获取时间 `System.currentTimeMillis()` “不准确“所困扰？ `QuantumClock` 既是为方便 Developer 同步App内时间。

## 原理

```kotlin
   currentTimeMillis = syncedTime + SystemClock.uptimeMillis() - lastSyncUpTime
```

- syncedTime: 上次同步的时间
- SystemClock.uptimeMillis() : 系统自开机以来运行的时间
- lastSyncUpTime： 上次同步时的 SystemClock.uptimeMillis()

## 使用

`QuantumClock` 内置了1个时间源：[淘宝](http://api.m.taobao.com/rest/api3.do?api=mtop.common.getTimestamp)。

在App启动时可以调用`sync()`来第一次同步
```
val job = QuantumClock.sync()
```
此时返回一个Kotlin协程中的`Job`对象，你可以用此来监听同步结果。允许设置多个同步器，直到有一个成功。在同步失败也会默认使用系统时间。

### 方法代替

| 原方法                       | 代替方法                         |
| ---------------------------- | -------------------------------- |
| `System.currentTimeMillis()` | `QuantumClock.currentTimeMillis` |
| `Calendar.getInstance()`     | `QuantumClock.nowCalendar`       |
| `Date()`                     | `QuantumClock.nowDate`           |

### 引入

```groovy



```

