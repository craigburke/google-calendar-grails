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
    }

    public int getDurationMinutes() {
        Minutes.minutesBetween(new DateTime(startTime), new DateTime(endTime)).minutes
    }

    void setIsRecurring(boolean value) {
        isRecurring = value

        if (!isRecurring) {
            clearAllRecurringValues()
        }
    }

    private void clearAllRecurringValues() {
        recurType = null
        recurCount = null
        recurInterval = null
        recurUntil = null
        excludeDays?.clear()
        recurDaysOfWeek?.clear()
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
