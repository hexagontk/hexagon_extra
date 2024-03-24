package com.hexagonkt.messaging.rabbitmq

import com.hexagonkt.converters.ConvertersManager
import com.hexagonkt.core.requirePath
import com.hexagonkt.messaging.Message
import com.hexagonkt.serialization.SerializationManager
import com.hexagonkt.serialization.jackson.json.Json
import com.hexagonkt.serialization.serialize
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.condition.DisabledInNativeImage
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName.parse
import java.lang.System.currentTimeMillis
import java.net.URI

@TestInstance(PER_CLASS)
@DisabledOnOs(OS.MAC, OS.WINDOWS)
@DisabledInNativeImage // TODO Fix for native image
internal class RabbitTest {

    data class Sample(val str: String, val int: Int) : Message()

    companion object {
        private val rabbitMq: RabbitMQContainer = RabbitMQContainer(parse("rabbitmq:3.8-alpine"))
            .withExposedPorts(5672)
            .apply { start() }

        val PORT: Int = rabbitMq.getMappedPort(5672)
        val URI: String = "amqp://guest:guest@localhost:$PORT"

        private const val QUEUE: String = "test"
        private const val QUEUE_ERROR: String = "error"
        private const val QUEUE_ERROR_SAMPLE: String = "errorSample"
        private const val SUFFIX: String = "DONE"
        private const val DELAY: Long = 10L
    }

    private val consumer: RabbitMqClient = RabbitMqClient(URI(URI))
    private val client: RabbitMqClient = RabbitMqClient(URI(URI))

    @BeforeAll fun startConsumer() {
        consumer.declareQueue(QUEUE)
        consumer.consume(QUEUE, String::class) { a ->
            Thread.sleep(DELAY)
            a + SUFFIX
        }

        consumer.declareQueue(QUEUE_ERROR)
        consumer.consume(QUEUE_ERROR, String::class) { a ->
            throw RuntimeException("Error with: $a")
        }
    }

    @AfterAll fun deleteTestQueue() {
        consumer.deleteQueue(QUEUE)
        consumer.deleteQueue(QUEUE_ERROR)
        consumer.deleteQueue(QUEUE_ERROR_SAMPLE)
        consumer.close()
    }

    @Test fun `Call return expected results` () {
        SerializationManager.defaultFormat = Json
        ConvertersManager.register(Long::class to String::class) { it.toString() }
        val ts = currentTimeMillis().toString()
        assert(client.call(QUEUE, ts) == ts + SUFFIX)
        val result = client.call(QUEUE_ERROR, ts)
        assert(result.contains(ts) && result.contains("Error with: $ts"))
    }

    @Test fun `Call errors` () {
        SerializationManager.defaultFormat = Json
        ConvertersManager.register(Map::class to Sample::class) {
            Sample(
                str = it.requirePath(Sample::str.name),
                int = it.requirePath(Sample::int.name),
            )
        }
        consumer.declareQueue(QUEUE_ERROR_SAMPLE)
        consumer.consume(QUEUE_ERROR_SAMPLE, Sample::class) {
            if (it.str == "no message error")
                error("")
            if (it.str == "message error")
                error("message")
        }

        client.publish(QUEUE_ERROR_SAMPLE, Sample("foo", 1).serialize())
        val result = client.call(QUEUE_ERROR_SAMPLE, Sample("no message error", 1).serialize())
        assert(result == IllegalStateException::class.java.name)

        // TODO Fix the case below
//        val result2 = client.call(QUEUE_ERROR, Sample("message error", 1).serialize())
//        assert(result2 == "message error")
    }
}
