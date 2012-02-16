import org.joda.time.DateTime
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.WEDNESDAY
import static org.joda.time.DateTimeConstants.FRIDAY

import com.craigburke.Event
import com.craigburke.EventRecurType

class BootStrap {

    def init = { servletContext ->
        def now = new DateTime()
        def tomorrow = now.plusDays(1)
        def nextMonday = now.withDayOfWeek(MONDAY).plusWeeks(1)
        
        // Creating a weekly event that occurs every MWF
        def event = new Event(title: 'Repeating MWF Event').with {
            startTime = now.toDate()
            endTime = now.plusHours(1).toDate()
            location = "Regular location"
            recurType = EventRecurType.WEEKLY
            [MONDAY, WEDNESDAY, FRIDAY]*.toInteger().each { addToRecurDaysOfWeek(it) }
            addToExcludeDays(nextMonday.withTime(0, 0, 0, 0).toDate())
            isRecurring = true
            save(flush: true)
        }

        // Non-repeating single event that replaces the one excluded next Monday
        def event2 = new Event(title:  'Repeating MWF Event (different location)').with {
            sourceEvent = event
            startTime = nextMonday.toDate()
            endTime = nextMonday.plusHours(1).toDate()
            location = "New one-time location"
            isRecurring = false
            save()
        }

        // Plain old non-repeating event
        def event3 = new Event(title: 'Just a normal event').with {
            startTime = tomorrow.toDate()
            endTime = tomorrow.plusMinutes(30).toDate()
            isRecurring = false
            save()
        }
    }

    def destroy = {
    }
}