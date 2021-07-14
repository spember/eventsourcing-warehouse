package io.cqrs.core.event

import com.fasterxml.jackson.databind.ObjectMapper
import demo.pember.eswarehouse.core.sku.SkuRegistered
import demo.pember.eswarehouse.postgres.tables.Events.EVENTS
import io.cqrs.core.identifiers.EntityId
import org.jooq.DSLContext
import org.jooq.JSONB
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

class PostgresEventRepository(private val jooq: DSLContext, private val objectMapper: ObjectMapper  ): EventRepository {

    override fun listAllByIds(vararg p0: EntityId<*>): List<EventEnvelope<out Event, out EntityId<*>>> {
        // construct a little lookup for all of the entityIds; when we retrieve the values from
        // disk, the raw values will be as string but we need the concrete type to pass into the
        // core data
        val entityIdLookup = mutableMapOf<String, EntityId<*>>()
        p0.forEach { entityId -> entityIdLookup[entityId.toString()] = entityId }

        return jooq.selectFrom(EVENTS)
            .where(EVENTS.ENTITY_ID.`in`(entityIdLookup.keys))
            .orderBy(EVENTS.REVISION.asc())
            .fetch()
            .groupBy { it.entityId }
            .flatMap { (rawId, eventRecords) ->
                eventRecords.map {
                    EventEnvelope(
                        objectMapper.readValue(it.data.data(), SkuRegistered::class.java),
                        EventCoreData(
                            entityIdLookup[rawId]!!, // we grouped by it so it should be here
                            it.revision,
                            it.instantObserved.toInstant(UTC),
                            it.instantOccurred.toInstant(UTC),
                            it.userId)
                    )
                }
            }
    }

    override fun write(p0: MutableList<EventEnvelope<out Event, out EntityId<*>>>) {
        val statement = jooq.insertInto(EVENTS,
            EVENTS.ENTITY_ID, EVENTS.REVISION, EVENTS.USER_ID,
            EVENTS.INSTANT_OBSERVED, EVENTS.INSTANT_OCCURRED, EVENTS.TYPE, EVENTS.DATA
        )
        p0.map {eventEnvelope -> Pair(eventEnvelope.eventCoreData, eventEnvelope.event) }
            .forEach { (coreData, event) -> statement.values(
                coreData.entityId.toString(), coreData.revision, coreData.createdBy,
                LocalDateTime.ofInstant(coreData.instantObserved, UTC),
                LocalDateTime.ofInstant(coreData.instantOccurred, UTC),
                event::class.java.simpleName,
                JSONB.jsonb(objectMapper.writeValueAsString(event))
            ) }

        statement.execute()
    }
}