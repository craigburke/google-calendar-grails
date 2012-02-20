var Calendar = {
    initCalendar: function() {
        $("#calendar").fullCalendar({
            events: 'list.json',
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            }
        });
    },

    init: function() {
        Calendar.initCalendar();
    }
};

$(Calendar.init);