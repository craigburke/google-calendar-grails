package com.craigburke

import org.joda.time.*
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.SUNDAY

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

    public findOccurrencesInRange = {Date rangeStart, Date rangeEnd ->
        def dates = []

        Date currentDate
        if (isRecurring) {
            currentDate = findNextOccurrence(rangeStart)

            while (currentDate && currentDate < rangeEnd) {
                dates.add(currentDate)
                Date nextDay = new DateTime(currentDate).plusDays(1).toDate()
                currentDate = findNextOccurrence(nextDay)
            }
        }
        // One time (non-recurring) event
        else {
            if (startTime >= rangeStart && endTime <= rangeEnd) {
                dates.add(startTime)
            }
        }

        dates
    }

    // For repeating event get next occurrence after the specified date
    private Date findNextOccurrence(Date afterDate) {
        Date nextOccurrence

        if (!isRecurring) {
            // non-repeating event
            nextOccurrence = null
        } else if (recurUntil && afterDate > recurUntil) {
            // Event is already over
            nextOccurrence = null
        } else if (afterDate < startTime) {
            // First occurrence
            if (recurType == EventRecurType.WEEKLY && !(isOnRecurringDay(startTime))) {
               Date nextDay = new DateTime(startTime).plusDays(1).toDate()
               nextOccurrence = findNextOccurrence(nextDay)
            }
            else {
                nextOccurrence = startTime
            }
        } else {
            switch (recurType) {

                case EventRecurType.DAILY:
                    nextOccurrence = findNextDailyOccurrence(afterDate)
                    break
                case EventRecurType.WEEKLY:
                    nextOccurrence = findNextWeeklyOccurrence(afterDate)
                    break
                case EventRecurType.MONTHLY:
                    nextOccurrence = findNextMonthlyOccurrence(afterDate)
                    break
                case EventRecurType.YEARLY:
                    nextOccurrence = findNextYearlyOccurrence(afterDate)
                    break
            }


        }

        if (isOnExcludedDay(nextOccurrence)) {
            // Skip this occurrence and go to the next one
            DateTime nextDay = (new DateTime(nextOccurrence)).plusDays(1)

            nextOccurrence = findNextOccurrence(nextDay.toDate())
        }
        else if (recurUntil && recurUntil <= nextOccurrence) {
            // Next occurrence happens after recurUntil date
            nextOccurrence = null
        }

        nextOccurrence
    }

    private Date findNextDailyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime(startTime)

        int daysBeforeDate = Days.daysBetween(new DateTime(startTime), new DateTime(afterDate)).getDays()
        int occurrencesBeforeDate = Math.floor(daysBeforeDate / recurInterval)

        nextOccurrence = nextOccurrence.plusDays((occurrencesBeforeDate + 1) * recurInterval)

        nextOccurrence.toDate()
    }


    private Date findNextWeeklyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime()
        int weeksBeforeDate = Weeks.weeksBetween(new DateTime(startTime), new DateTime(afterDate)).getWeeks()
        int weekOccurrencesBeforeDate = Math.floor(weeksBeforeDate / recurInterval)

        DateTime lastOccurrence = new DateTime(startTime)
        lastOccurrence = lastOccurrence.plusWeeks(weekOccurrencesBeforeDate * recurInterval)
        lastOccurrence = lastOccurrence.withDayOfWeek(MONDAY)

        if (isInSameWeek(lastOccurrence.toDate(), afterDate)) {
            nextOccurrence = lastOccurrence.plusDays(1)
        }
        else {
            nextOccurrence = lastOccurrence
        }

        boolean occurrenceFound = false

        while (!occurrenceFound) {
            if (nextOccurrence.toDate() >= afterDate && isOnRecurringDay(nextOccurrence.toDate())) {
                occurrenceFound = true
            }
            else {
                if (nextOccurrence.dayOfWeek() == SUNDAY) {
                    // we're about to pass into the next week
                    nextOccurrence = nextOccurrence.plusDays(1).plusWeeks(recurInterval)
                }
                else {
                    nextOccurrence = nextOccurrence.plusDays(1)
                }
            }

        }

        nextOccurrence.toDate()
    }

    private Date findNextMonthlyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime(startTime)

        int monthsBeforeDate = Months.monthsBetween(new DateTime(startTime), new DateTime(afterDate)).getMonths()
        int occurrencesBeforeDate = Math.floor(monthsBeforeDate / recurInterval)
        nextOccurrence = nextOccurrence.plusMonths((occurrencesBeforeDate + 1) * recurInterval)

        nextOccurrence.toDate()
    }

    private Date findNextYearlyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime(startTime)

        int yearsBeforeDate = Years.yearsBetween(new DateTime(startTime), new DateTime(afterDate)).getYears()
        int occurrencesBeforeDate = Math.floor(yearsBeforeDate / recurInterval)
        nextOccurrence = nextOccurrence.plusYears((occurrencesBeforeDate + 1) * recurInterval)

        nextOccurrence.toDate()
    }


    private boolean isInSameWeek(Date date1, Date date2) {
        DateTime dateTime1 = new DateTime(date1)
        DateTime dateTime2 = new DateTime(date2)

        ((Weeks.weeksBetween(dateTime1, dateTime2)).weeks == 0)
    }

    private boolean isOnSameDay(Date date1, Date date) {
        DateTime dateTime1 = new DateTime(date1)
        DateTime dateTime2 = new DateTime(date2)

        ((Days.daysBetween(dateTime1, dateTime2)).days == 0)
    }

    private boolean isOnRecurringDay(Date date) {
       int day = new DateTime(date).getDayOfWeek()
        
       recurDaysOfWeek.find{it == day}
    }

    private def isOnExcludedDay = {Date date ->
        boolean result = false
        date = (new DateTime(date)).withTime(0, 0, 0, 0).toDate()
        excludeDays.contains(date)
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
