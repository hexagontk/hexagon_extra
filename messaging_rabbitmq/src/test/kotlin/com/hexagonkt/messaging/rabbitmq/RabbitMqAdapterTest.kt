package com.hexagonkt.messaging.rabbitmq

import com.hexagonkt.messaging.MessagingPort
import com.hexagonkt.messaging.rabbitmq.RabbitTest.Companion.PORT
import com.hexagonkt.serialization.jackson.json.Json
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS
import kotlin.test.Test

@DisabledOnOs(OS.MAC, OS.WINDOWS)
internal class RabbitMqAdapterTest {
    /**
     * TODO Add asserts
     */
    @Test fun `Event manager` () {
        val engine: MessagingPort = RabbitMqAdapter("amqp://guest:guest@localhost:$PORT", Json)
        engine.consume(RabbitTest.Sample::class) {
            if (it.str == "no message error")
                throw IllegalStateException()
            if (it.str == "message error")
                error("message")
        }

        engine.publish(RabbitTest.Sample("foo", 1))
//        EventManager.publish(Sample("no message error", 1))
//        EventManager.publish(Sample("message error", 1))
    }
}
