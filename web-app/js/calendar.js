$(document).ready(function() {
    $("#calendar").fullCalendar({
        events: 'list',
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
        }
    });

});