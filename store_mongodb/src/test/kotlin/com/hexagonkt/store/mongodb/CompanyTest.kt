package com.hexagonkt.store.mongodb

import com.hexagonkt.converters.ConvertersManager
import com.hexagonkt.converters.convertObjects
import com.hexagonkt.core.fieldsMapOf
import com.hexagonkt.core.getPath
import com.hexagonkt.core.requirePath
import com.hexagonkt.store.Store
import com.hexagonkt.store.mongodb.Department.DESIGN
import com.hexagonkt.store.mongodb.Department.DEVELOPMENT
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
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
            Person(name = it.requirePath(Person::name.name))
        }
        ConvertersManager.register(Person::class to Map::class) {
            mapOf(Person::name.name to it.name)
        }

        ConvertersManager.register(String::class to Department::class) { Department.valueOf(it) }

        ConvertersManager.register(Map::class to Company::class) { m ->
            Company(
                id = m.requirePath(Company::id),
                foundation = m.requirePath<LocalDateTime>(Company::foundation).toLocalDate(),
                closeTime = m.requirePath<LocalDateTime>(Company::closeTime).toLocalTime(),
                openTime = m.requirePath<Map<*,*>>(Company::openTime).let { t ->
                    val s = t.requirePath<LocalDateTime>(ClosedRange<*>::start).toLocalTime()
                    val e = t.requirePath<LocalDateTime>(ClosedRange<*>::endInclusive).toLocalTime()
                    s..e
                },
                web = URL(m.requirePath(Company::web)),
                clients = m.getPath<List<String>>(Company::clients)?.map { URL(it) } ?: emptyList(),
                logo = m.getPath(Company::logo),
                notes = m.getPath(Company::notes),
                people = m.getPath<List<Map<*, *>>>(Company::people)
                    ?.convertObjects(Person::class)
                    ?.toSet()
                    ?: emptySet(),
                departments = m.getPath<List<String>>(Company::departments)
                    ?.convertObjects(Department::class)
                    ?.toSet()
                    ?: emptySet(),
                creationDate = m.requirePath(Company::creationDate),
            )
        }

        ConvertersManager.register(Company::class to Map::class) { c ->
            fieldsMapOf(
                Company::id to c.id,
                Company::foundation to c.foundation,
                Company::closeTime to c.closeTime,
                Company::openTime to fieldsMapOf(
                    ClosedRange<*>::start to c.openTime.start,
                    ClosedRange<*>::endInclusive to c.openTime.endInclusive,
                ),
                Company::web to c.web,
                Company::clients to c.clients.map { it.toString() },
                Company::logo to c.logo,
                Company::notes to c.notes,
                Company::people to c.people.map { mapOf(Person::name.name to it.name) },
                Company::departments to c.departments,
                Company::creationDate to c.creationDate,
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
