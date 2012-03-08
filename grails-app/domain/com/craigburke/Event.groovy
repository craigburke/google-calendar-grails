package com.craigburke

import org.joda.time.DateTime
import org.joda.time.Minutes

class Event {
    
    String title
    String location
    String description

    Date startTime
    Date endTime

    // Recurring Options
    boolean isRecurring
    EventRecurType recurType
    Integer recurInterval = 1
    Date recurUntil
    Integer recurCount

    Event sourceEvent

    static hasMany = [recurDaysOfWeek: Integer, excludeDays: Date]
    static transients = ['durationMinutes']

    def eventService

    static constraints = {
        title(nullable: false, blank: false)
        location(nullable: true, blank:  true)
        description(nullable: true, blank: true)
        recurType(nullable: true)
        recurInterval(nullable: true)
        recurUntil(nullable: true)
        recurCount(nullable: true)
        startTime(nullable: false)
        excludeDays(nullable: true)
        sourceEvent(nullable: true)
        startTime(required: true, nullable: false)
        endTime(required: true, nullable: false, validator: {val, obj -> val > obj.startTime} )
    }

    public int getDurationMinutes() {
        Minutes.minutesBetween(new DateTime(startTime), new DateTime(endTime)).minutes
    }

    private void updateRecurringValues() {
        if (!isRecurring) {
            recurType = null
            recurCount = null
            recurInterval = null
            recurUntil = null
            excludeDays?.clear()
            recurDaysOfWeek?.clear()
        }

        // Set recurUntil date based on the recurCount value
        if (recurCount && !recurUntil) {
           Date recurCountDate = startTime

           for (int i in 1..recurCount) {
               recurCountDate = eventService.findNextOccurrence(this, recurCountDate)
           }

           recurUntil = recurCountDate
        }
        
    }

    def beforeUpdate() {
        updateRecurringValues()
    }
    
    def beforeInsert() {
        updateRecurringValues()
    }
    
    def beforeDelete() {
        Event.executeUpdate("UPDATE Event E SET E.sourceEvent = null WHERE E.sourceEvent.id = :eventId", [eventId: this.id])
    }
    

}


public enum EventRecurType {
    DAILY('Daily'),
    WEEKLY('Weekly'),
    MONTHLY('Monthly'),
    YEARLY('Yearly')

    String name

    EventRecurType(String name) {
        this.name = name
    }
}
