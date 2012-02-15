package com.craigburke

import org.joda.time.DateTime
import org.joda.time.Instant

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class EventController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {

    }
   
    def list = {
        Date startRange = new Instant(params.long('start') * 1000L).toDate()
        Date endRange = new Instant(params.long('end') * 1000L).toDate()

        String hql = """\
            FROM Event WHERE
            ((isRecurring = false AND (startTime between :startRange AND :endRange)) OR
            (isRecurring = true AND (recurUntil is null OR recurUntil >= :startRange)))
        """

        def namedParameters = [startRange: startRange, endRange: endRange]
        def events = Event.executeQuery(hql, namedParameters)

        def eventList = []
        
        // iterate through to see if we need to add additional Event instances because of recurring
        // events
        events.each {event ->

            def dates = event.findOccurrencesInRange(startRange, endRange)
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

        render eventList as JSON
    }

    def create = {
        [eventInstance: new Event(params)]
    }

    def save = {
        def eventInstance = new Event(params)
        if (!eventInstance.save(flush: true)) {
            render(view: "create", model: [eventInstance: eventInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'event.label', default: 'Event'), eventInstance.id])
        redirect(action: "show", id: eventInstance.id)
    }

    def show = {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
            return
        }

        [eventInstance: eventInstance]
    }

    def edit = {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
            return
        }

        [eventInstance: eventInstance]
    }

    def update = {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (eventInstance.version > version) {
                eventInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'event.label', default: 'Event')] as Object[],
                          "Another user has updated this Event while you were editing")
                render(view: "edit", model: [eventInstance: eventInstance])
                return
            }
        }

        eventInstance.properties = params

        if (!eventInstance.save(flush: true)) {
            render(view: "edit", model: [eventInstance: eventInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'event.label', default: 'Event'), eventInstance.id])
        redirect(action: "show", id: eventInstance.id)
    }

    def delete = {
        def eventInstance = Event.get(params.id)
        if (!eventInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
            return
        }

        try {
            eventInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'event.label', default: 'Event'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
