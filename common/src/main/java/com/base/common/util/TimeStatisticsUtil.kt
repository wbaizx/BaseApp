package com.base.common.util

private const val STATUS_RUNING = 0
private const val STATUS_PAUSE = 1
private const val STATUS_FINISH = 2

class TimeStatisticsUtil {
    private var status = STATUS_FINISH

    private var time = 0L
    private var recordPoint = 0L

    fun start() {
        recordPoint = System.currentTimeMillis()
        status = STATUS_RUNING
    }

    fun resume() {
        if (status == STATUS_PAUSE) {
            recordPoint = System.currentTimeMillis()
            status = STATUS_RUNING
        }
    }

    fun pause() {
        if (status == STATUS_RUNING) {
            time += System.currentTimeMillis() - recordPoint
            status = STATUS_PAUSE
        }
    }

    fun reset() {
        time = 0L
        status = STATUS_FINISH
    }

    fun getDuration(): Long {
        return when (status) {
            STATUS_RUNING -> System.currentTimeMillis() - recordPoint + time
            STATUS_PAUSE -> time
            else -> 0
        }
    }
}