<%@ page import="com.craigburke.Event" %>



<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'title', 'error')} required">
    <label for="title">
        <g:message code="event.title.label" default="Title"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="title" required="" value="${eventInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'location', 'error')} ">
    <label for="location">
        <g:message code="event.location.label" default="Location"/>

    </label>
    <g:textField name="location" value="${eventInstance?.location}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'description', 'error')} ">
    <label for="description">
        <g:message code="event.description.label" default="Description"/>

    </label>
    <g:textField name="description" value="${eventInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'recurType', 'error')} ">
    <label for="recurType">
        <g:message code="event.recurType.label" default="Recur Type"/>

    </label>
    <g:select name="recurType" from="${com.craigburke.EventRecurType?.values()}"
              keys="${com.craigburke.EventRecurType.values()*.name()}" value="${eventInstance?.recurType?.name()}"
              noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'recurInterval', 'error')} ">
    <label for="recurInterval">
        <g:message code="event.recurInterval.label" default="Recur Interval"/>

    </label>
    <g:field type="number" name="recurInterval" value="${fieldValue(bean: eventInstance, field: 'recurInterval')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'recurUntil', 'error')} ">
    <label for="recurUntil">
        <g:message code="event.recurUntil.label" default="Recur Until"/>

    </label>
    <g:datePicker name="recurUntil" precision="day" value="${eventInstance?.recurUntil}" default="none"
                  noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'recurCount', 'error')} ">
    <label for="recurCount">
        <g:message code="event.recurCount.label" default="Recur Count"/>

    </label>
    <g:field type="number" name="recurCount" value="${fieldValue(bean: eventInstance, field: 'recurCount')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'startTime', 'error')} required">
    <label for="startTime">
        <g:message code="event.startTime.label" default="Start Time"/>
        <span class="required-indicator">*</span>
    </label>
    <g:datePicker name="startTime" precision="day" value="${eventInstance?.startTime}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'excludeDays', 'error')} ">
    <label for="excludeDays">
        <g:message code="event.excludeDays.label" default="Exclude Days"/>

    </label>

</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'recurDaysOfWeek', 'error')} ">
    <label for="recurDaysOfWeek">
        <g:message code="event.recurDaysOfWeek.label" default="Recur Days Of Week"/>

    </label>

</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'endTime', 'error')} required">
    <label for="endTime">
        <g:message code="event.endTime.label" default="End Time"/>
        <span class="required-indicator">*</span>
    </label>
    <g:datePicker name="endTime" precision="day" value="${eventInstance?.endTime}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'isRecurring', 'error')} ">
    <label for="isRecurring">
        <g:message code="event.isRecurring.label" default="Is Recurring"/>

    </label>
    <g:checkBox name="isRecurring" value="${eventInstance?.isRecurring}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'sourceEvent', 'error')} required">
    <label for="sourceEvent">
        <g:message code="event.sourceEvent.label" default="Source Event"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select id="sourceEvent" name="sourceEvent.id" from="${com.craigburke.Event.list()}" optionKey="id" required=""
              value="${eventInstance?.sourceEvent?.id}" class="many-to-one"/>
</div>

