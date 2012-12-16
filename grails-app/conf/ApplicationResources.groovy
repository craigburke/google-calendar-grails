modules = {
    core {
        resource url:'/js/jquery-1.7.1.min.js', disposition: 'head'
        resource url:'/js/jquery-ui-1.8.18.custom.min.js', disposition: 'head'

        resource url: '/css/smoothness/jquery-ui-1.8.18.custom.css'
    }

    home {
        resource url:'/css/home.css'
    }

    datePicker {
        dependsOn 'core'
        resource url: '/js/jquery-ui-timepicker-addon.js'
    }


    fullCalendar {
        dependsOn 'core'
        resource url:'/js/fullcalendar.min.js'
        resource url:'/css/fullcalendar.css'
    }

    qtip {
        dependsOn 'core'

        resource url: '/js/jquery.qtip.min.js'
        resource url: '/css/jquery.qtip.min.css'
    }


    calendar {
        dependsOn 'fullCalendar, datePicker, qtip'

        resource url: '/js/calendar.js'
        resource url: '/css/calendar.css'
    }
}