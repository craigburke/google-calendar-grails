package com.craigburke

import org.joda.time.DateTime
import org.joda.time.Instant

import grails.converters.JSON
import java.text.SimpleDateFormat
import org.joda.time.Minutes

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
                        start: startTime.toString(),
                        end: endTime.toString()
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

    def create = {
        def eventInstance = new Event()
        eventInstance.properties = params

        [eventInstance: eventInstance]
    }


    def show = {
        def (startTime, endTime) = [params.long('startTime'), params.long('endTime')]
        def eventInstance = Event.get(params.id)

        if (!eventInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])}"
            redirect(action: "index")
        }
        else {
            def model = [eventInstance: eventInstance, startTime: startTime, endTime: endTime]

            if (request.xhr) {
                render(template: "showPopup", model: model)
            }
            else {
                model
            }
        }

    }

    def save = {
        def eventInstance = new Event(params)

        if (eventInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'event.label', default: 'Event'), eventInstance.id])}"
            redirect(action: "show", id: eventInstance.id)
        }
        else {
            render(view: "create", model: [eventInstance: eventInstance])
        }

    }

    def edit = {
        def eventInstance = Event.get(params.id)
        def (startTime, endTime) = [params.long('startTime'), params.long('endTime')]

        if (!eventInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])}"
            redirect(action: "index")
        }
        else {
            [eventInstance: eventInstance, startTime: startTime, endTime: endTime]
        }

    }

    def update = {
        def eventInstance = Event.get(params.id)
        String editType = params.editType

        if (eventInstance && !eventInstance.isRecurring) {
            eventInstance.properties = params
            if (!eventInstance.hasErrors() && eventInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'event.label', default: 'Event'), eventInstance.id])}"
                redirect(action: "index")
            }
            else {
                render(view: "edit", model: [eventInstance: eventInstance, startTime: startTime, endTime: endTime])
            }
        }
        else if (eventInstance && editType) {
            Date startTime = params.date('startTime', ['MM/dd/yyyy hh:mm a'])
            Date endTime = params.date('endTime', ['MM/dd/yyyy hh:mm a'])

            // Using the date from the original startTime and endTime with the update time from the form
            Date updatedStartTime = new DateTime(eventInstance.startTime).withTime(startTime.hours, startTime.minutes, 0, 0).toDate()
            Date updatedEndTime = new DateTime(updatedStartTime).plusMinutes(Minutes.between(new DateTime(startTime), new DateTime(endTime)).minutes)

            if (editType == "occurrence") {
                // Add an exclusion
                eventInstance.with {
                    addToExcludeDays(new DateTime(startTime).withTime(0, 0, 0, 0).toDate())
                    save(flush: true)
                }

                // single event
                new Event(params).with {
                    startTime = updatedStartTime
                    endTime = updatedEndTime
                    isRecurring = false // ignore recurring options this is a single event
                    save(flush: true)
                }
            }
            else if (editType == "following") {
                // following event
                new Event(params).with {
                    recurUntil = eventInstance.recurUntil
                    save(flush: true)
                }

                eventInstance.with {
                    recurUntil = startTime
                    save(flush: true)
                }
            }
            else if (editType == "all") {
                eventInstance.properties = params
                eventInstance.save(flush: true)
            }
            
            redirect(action: "index")
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])}"
            redirect(action: "index")
        }
    }


    def delete = {
        def eventInstance = Event.get(params.id)
        String deleteType = params.deleteType

        if (eventInstance && (!eventInstance.isRecurring || deleteType == "all")) {
            eventInstance.delete(flush: true)
            flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'event.label', default: 'Event'), params.id])}"
            redirect(action: "index")
        }
        else if (eventInstance && deleteType) {
            def startTime = new Instant(params.long('startTime')).toDate()

            if (deleteType == "occurrence") {
                // Add an exclusion
                eventInstance.addToExcludeDays(new DateTime(startTime).withTime(0, 0, 0, 0).toDate())
                eventInstance.save(flush: true);
            }
            else if (deleteType == "following") {
                eventInstance.recurUntil = startTime
                eventInstance.save(flush: true)
            }

            redirect(action: "index")
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'event.label', default: 'Event'), params.id])}"
            redirect(action: "index")
        }



    }

    
}