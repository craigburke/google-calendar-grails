package com.craigburke

import static org.joda.time.DateTimeConstants.SUNDAY
import static org.joda.time.DateTimeConstants.MONDAY
import static org.joda.time.DateTimeConstants.TUESDAY
import static org.joda.time.DateTimeConstants.WEDNESDAY
import static org.joda.time.DateTimeConstants.THURSDAY
import static org.joda.time.DateTimeConstants.FRIDAY
import static org.joda.time.DateTimeConstants.SATURDAY


class CalendarTagLib {
    static namespace = "calendar"

    def daysOfWeek = {attr, body ->
        def days = [
            [key: SUNDAY, value: 'Sunday'],
            [key: MONDAY, value: 'Monday'],
            [key: TUESDAY, value: 'Tuesday'],
            [key: WEDNESDAY, value: 'Wednesday'],
            [key: THURSDAY, value: 'Thursday'],
            [key: FRIDAY, value: 'Friday'],
            [key: SATURDAY, value: 'Saturday']
        ]
        
        def selectedDays = attr.selectedDays
        def name = attr.name

        days.eachWithIndex { def day, int index ->
            def id = "${name}_${index}"
            
            out << g.checkBox(name: name, id: id, value: day.key, checked: (selectedDays?.contains(day.key)), title: day.value)
            out << "<label for='${id}'>${day.value[0..0]}</label>"
        }
    }

}
