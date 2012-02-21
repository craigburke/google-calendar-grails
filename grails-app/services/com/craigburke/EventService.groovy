package com.craigburke

import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Weeks
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.SUNDAY
import org.joda.time.Months
import org.joda.time.Years

class EventService {

    private Event event

    def findOccurrencesInRange = {Event event, Date rangeStart, Date rangeEnd ->
        this.event = event

        def dates = []

        Date currentDate
        if (event.isRecurring) {
            currentDate = findNextOccurrence(rangeStart)

            while (currentDate && currentDate < rangeEnd) {
                dates.add(currentDate)
                Date nextDay = new DateTime(currentDate).plusDays(1).toDate()
                currentDate = findNextOccurrence(nextDay)
            }
        }
        // One time (non-recurring) event
        else {
            if (event.startTime >= rangeStart && event.endTime <= rangeEnd) {
                dates.add(event.startTime)
            }
        }

        dates
    }

    // For repeating event get next occurrence after the specified date
    private Date findNextOccurrence(Date afterDate) {
        Date nextOccurrence

        if (!event.isRecurring) {
            // non-repeating event
            nextOccurrence = null
        } else if (event.recurUntil && afterDate > event.recurUntil) {
            // Event is already over
            nextOccurrence = null
        } else if (afterDate < event.startTime) {
            // First occurrence
            if (event.recurType == EventRecurType.WEEKLY && !(isOnRecurringDay(event.startTime))) {
                Date nextDay = new DateTime(event.startTime).plusDays(1).toDate()
                nextOccurrence = findNextOccurrence(nextDay)
            }
            else {
                nextOccurrence = event.startTime
            }
        } else {
            switch (event.recurType) {

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
        else if (event.recurUntil && event.recurUntil <= nextOccurrence) {
            // Next occurrence happens after recurUntil date
            nextOccurrence = null
        }

        nextOccurrence
    }

    private Date findNextDailyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime(event.startTime)

        int daysBeforeDate = Days.daysBetween(new DateTime(event.startTime), new DateTime(afterDate)).getDays()
        int occurrencesBeforeDate = Math.floor(daysBeforeDate / event.recurInterval)

        nextOccurrence = nextOccurrence.plusDays((occurrencesBeforeDate + 1) * event.recurInterval)

        nextOccurrence.toDate()
    }


    private Date findNextWeeklyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime()
        int weeksBeforeDate = Weeks.weeksBetween(new DateTime(event.startTime), new DateTime(afterDate)).getWeeks()
        int weekOccurrencesBeforeDate = Math.floor(weeksBeforeDate / event.recurInterval)

        DateTime lastOccurrence = new DateTime(event.startTime)
        lastOccurrence = lastOccurrence.plusWeeks(weekOccurrencesBeforeDate * event.recurInterval)
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
                    nextOccurrence = nextOccurrence.plusDays(1).plusWeeks(event.recurInterval)
                }
                else {
                    nextOccurrence = nextOccurrence.plusDays(1)
                }
            }

        }

        nextOccurrence.toDate()
    }

    private Date findNextMonthlyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime(event.startTime)

        int monthsBeforeDate = Months.monthsBetween(new DateTime(event.startTime), new DateTime(afterDate)).getMonths()
        int occurrencesBeforeDate = Math.floor(monthsBeforeDate / event.recurInterval)
        nextOccurrence = nextOccurrence.plusMonths((occurrencesBeforeDate + 1) * event.recurInterval)

        nextOccurrence.toDate()
    }

    private Date findNextYearlyOccurrence(Date afterDate) {
        DateTime nextOccurrence = new DateTime(event.startTime)

        int yearsBeforeDate = Years.yearsBetween(new DateTime(event.startTime), new DateTime(afterDate)).getYears()
        int occurrencesBeforeDate = Math.floor(yearsBeforeDate / event.recurInterval)
        nextOccurrence = nextOccurrence.plusYears((occurrencesBeforeDate + 1) * event.recurInterval)

        nextOccurrence.toDate()
    }


    private boolean isInSameWeek(Date date1, Date date2) {
        DateTime dateTime1 = new DateTime(date1)
        DateTime dateTime2 = new DateTime(date2)

        ((Weeks.weeksBetween(dateTime1, dateTime2)).weeks == 0)
    }

    private boolean isOnSameDay(Date date1, Date date2) {
        DateTime dateTime1 = new DateTime(date1)
        DateTime dateTime2 = new DateTime(date2)

        ((Days.daysBetween(dateTime1, dateTime2)).days == 0)
    }

    private boolean isOnRecurringDay(Date date) {
        int day = new DateTime(date).getDayOfWeek()

        event.recurDaysOfWeek.find{it == day}
    }

    private def isOnExcludedDay = {Date date ->
        boolean result = false
        date = (new DateTime(date)).withTime(0, 0, 0, 0).toDate()
        event.excludeDays.contains(date)
    }
}
