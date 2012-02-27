package com.craigburke

import org.joda.time.DateTime
import org.joda.time.Instant

import grails.converters.JSON

class EventController {
    def eventService

    def index = {

    }

    def list = {
        def (startRange, endRange) = [params.long('start'), params.long('end')].collect { new Instant(it  * 1000L).toDate() }

        def events = Event.withCriteria {
            or {
                and {
                    eq("isRecurring", false)
                    between("startTime", startRange, endRange)
                }
                and {
                    eq("isRecurring", true)
                    or {
                        isNull("recurUntil")
                        ge("recurUntil", startRange)
                    }
                }
            }
        }

        // iterate through to see if we need to add additional Event instances because of recurring
        // events
        def eventList = []
        events.each {event ->

            def dates = eventService.findOccurrencesInRange(event, startRange, endRange)

            dates.each { date ->
                DateTime startTime = new DateTime(date)
                DateTime endTime = startTime.plusMinutes(event.durationMinutes)

                eventList << [
                        id: event.id,
                        title: event.title,
                        allDay: false,
                        start: (startTime.toInstant().millis / 1000L),
                        end: (endTime.toInstant().millis / 1000L)
                ]
            }
        }

        withFormat {
            html {
                [eventInstanceList: eventList]
            }
            json {
                render eventList as JSON
            }
        }
    }
}