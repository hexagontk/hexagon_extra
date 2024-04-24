package com.hexagonkt.messaging

import java.lang.System.currentTimeMillis
import java.lang.Thread.currentThread
import java.time.LocalDateTime

import com.hexagonkt.core.toNumber
import com.hexagonkt.core.Jvm

open class Message (
    val timestamp: Long = currentTimeMillis(),
    val dateTime: Long = LocalDateTime.now().toNumber(),
    val hostname: String = Jvm.hostName,
    val ip: String = Jvm.ip,
//    val jvmId: String = Jvm.id,
    val thread: String = currentThread().name
)
