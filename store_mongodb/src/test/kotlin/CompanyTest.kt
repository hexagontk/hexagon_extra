package com.hexagonkt.store.mongodb

import com.hexagonkt.core.converters.ConvertersManager
import com.hexagonkt.core.converters.convertObjects
import com.hexagonkt.core.get
import com.hexagonkt.core.requireKeys
import com.hexagonkt.store.Store
import com.hexagonkt.store.mongodb.Department.DESIGN
import com.hexagonkt.store.mongodb.Department.DEVELOPMENT
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URL
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@TestInstance(PER_CLASS)
internal class CompanyTest : StoreTest<Company, String>() {
    private val company = Company(
        id = "id",
        foundation = LocalDate.of(2014, 1, 25),
        closeTime = LocalTime.of(11, 42),
        openTime = LocalTime.of(8, 30)..LocalTime.of(14, 51),
        web = URL("http://example.org"),
        clients = listOf(
            URL("http://c1.example.org"),
            URL("http://c2.example.org")
        ),
        logo = byteArrayOf(0, 1, 2),
        notes = "notes",
        people = setOf(
            Person(name = "John"),
            Person(name = "Mike")
        ),
        departments = setOf(DESIGN, DEVELOPMENT),
        creationDate = LocalDateTime.of(2016, 1, 1, 0, 0, 0)
    )

    private val company1 = Company(
        id = "id1",
        foundation = LocalDate.of(2014, 1, 25),
        closeTime = LocalTime.of(11, 42),
        openTime = LocalTime.of(8, 30)..LocalTime.of(14, 36),
        web = URL("http://example.org"),
        people = setOf(
            Person(name = "John"),
            Person(name = "Mike")
        )
    )

    override fun createTestEntities(): List<Company> = listOf(company, company1)

    private val mongodbUrl by lazy { "mongodb://localhost:${mongoDb.getMappedPort(27017)}/test" }

    override fun createStore(): Store<Company, String> =
        MongoDbStore(Company::class, Company::id, mongodbUrl, "companies")

    override fun changeObject(obj: Company) =
        obj.copy(web = URL("http://change.example.org"))

    @BeforeAll fun initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

        ConvertersManager.register(Map::class to Person::class) {
            Person(name = it.requireKeys(Person::name.name))
        }
        ConvertersManager.register(Person::class to Map::class) {
            mapOf(Person::name.name to it.name)
        }

        ConvertersManager.register(String::class to Department::class) { Department.valueOf(it) }

        ConvertersManager.register(Map::class to Company::class) { m ->
            Company(
                id = m.requireKeys(Company::id.name),
                foundation = m.requireKeys<LocalDateTime>(Company::foundation.name).toLocalDate(),
                closeTime = m.requireKeys<LocalDateTime>(Company::closeTime.name).toLocalTime(),
                openTime = m.requireKeys<Map<*,*>>(Company::openTime.name).let { otm ->
                    val s = otm.requireKeys<LocalDateTime>(ClosedRange<*>::start.name).toLocalTime()
                    val e = otm.requireKeys<LocalDateTime>(ClosedRange<*>::endInclusive.name).toLocalTime()
                    s..e
                },
                web = URL(m.requireKeys(Company::web.name)),
                clients = (m[Company::clients.name] as? List<String>)?.map { URL(it) } ?: emptyList(),
                logo = m[Company::logo.name] as? ByteArray,
                notes = m[Company::notes.name] as? String,
                people = (m[Company::people.name] as? List<Map<*, *>>)?.convertObjects(Person::class)?.toSet() ?: emptySet(),
                departments = (m[Company::departments.name] as? List<String>)?.convertObjects(Department::class)?.toSet() ?: emptySet(),
                creationDate = m.requireKeys(Company::creationDate.name),
            )
        }
        ConvertersManager.register(Company::class to Map::class) { c ->
            mapOf(
                Company::id.name to c.id,
                Company::foundation.name to c.foundation,
                Company::closeTime.name to c.closeTime,
                Company::openTime.name to mapOf(
                    ClosedRange<*>::start.name to c.openTime.start,
                    ClosedRange<*>::endInclusive.name to c.openTime.endInclusive,
                ),
                Company::web.name to c.web,
                Company::clients.name to c.clients.map { it.toString() },
                Company::logo.name to c.logo,
                Company::notes.name to c.notes,
                Company::people.name to c.people.map { mapOf(Person::name.name to it.name) },
                Company::departments.name to c.departments,
                Company::creationDate.name to c.creationDate,
            )
        }
    }

    @Test fun `New records are stored`() {
        new_records_are_stored()
    }

    @Test fun `Many records are stored`() {
        many_records_are_stored()
    }

    @Test fun `Entities are stored`() {
        entities_are_stored()
    }

    @Test fun `Insert one record returns the proper key`() {
        insert_one_record_returns_the_proper_key()
    }
}
