package com.hexagonkt.messaging.rabbitmq

import com.hexagonkt.messaging.Message
import com.hexagonkt.messaging.MessagingPort
import com.hexagonkt.serialization.SerializationFormat
import com.hexagonkt.serialization.serialize
import java.net.URI
import kotlin.reflect.KClass

class RabbitMqAdapter(
    url: String = "amqp://guest:guest@localhost",
    private val serializationFormat: SerializationFormat
) : MessagingPort {

    private companion object {
        private const val EXCHANGE = "messages"
    }

    private val client by lazy { RabbitMqClient(URI(url), serializationFormat) }

    init {
        client.bindExchange(EXCHANGE, "topic", "*.*.*", "event_pool")
    }

    override fun <T : Message> consume(type: KClass<T>, address: String, consumer: (T) -> Unit) {
        client.consume(EXCHANGE, address, type) { consumer(it) }
    }

    override fun publish(message: Message, address: String) {
        client.publish(EXCHANGE, address, message.serialize(serializationFormat))
    }
}
