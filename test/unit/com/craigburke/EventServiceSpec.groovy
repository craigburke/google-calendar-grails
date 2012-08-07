package com.craigburke

import grails.test.mixin.*
import grails.plugin.spock.*
import spock.lang.*

import org.joda.time.*
import static org.joda.time.DateTimeConstants.*

@TestFor(EventService)
@Mock(Event)
class EventServiceSpec extends UnitSpec {

    @Shared DateTime now
    @Shared DateTime mondayNextWeek
    @Shared DateTime wednesdayNextWeek
    @Shared DateTime fridayNextWeek
    @Shared DateTime mondayAfterNext

    @Shared Event mwfEvent

    def setupSpec() {
        now = new DateTime()
        mondayNextWeek = new DateTime().plusWeeks(1).withDayOfWeek(MONDAY).withTime(0,0,0,0)
        wednesdayNextWeek = mondayNextWeek.withDayOfWeek(WEDNESDAY)
        fridayNextWeek = mondayNextWeek.withDayOfWeek(FRIDAY)
        mondayAfterNext = mondayNextWeek.plusWeeks(1)

        mwfEvent = new Event(
                title: 'Repeating MWF Event',
                startTime: mondayNextWeek.toDate(),
                endTime: mondayNextWeek.plusHours(1).toDate(),
                location: "Regular location",
                recurType: EventRecurType.WEEKLY,
                isRecurring: true,
                recurDaysOfWeek: [MONDAY, WEDNESDAY, FRIDAY]
        )

    }

    @Unroll("next occurance of weekly event after #afterDate")
    def "next occurrence of a weekly event without excluded days"() {
        expect:
            service.findNextOccurrence(event, afterDate.toDate()) == expectedResult.toDate()

        where:
            event    | afterDate         | expectedResult
            mwfEvent | now               | mondayNextWeek
            mwfEvent | mondayNextWeek    | wednesdayNextWeek
            mwfEvent | wednesdayNextWeek | fridayNextWeek
    }

    @Unroll("next occurence of weekly event with exclusion after #afterDate")
    def "test exclusion of next monday"() {
        setup:
            mwfEvent.addToExcludeDays(mondayNextWeek.toDate())

        expect:
            service.findNextOccurrence(event, afterDate.toDate()) == expectedResult.toDate()


        where:
            event    | afterDate           | expectedResult
            mwfEvent | now                 | wednesdayNextWeek
            mwfEvent | mondayNextWeek      | wednesdayNextWeek
            mwfEvent | wednesdayNextWeek   | fridayNextWeek

    }



}
