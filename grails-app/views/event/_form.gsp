<%@ page import="com.craigburke.Event" %>
<%@ page import="com.craigburke.EventRecurType" %>
<%@ page import="org.joda.time.Instant" %>


<g:set var="entityName" value="${message(code: 'event.label', default: 'Event')}" />

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'title', 'error')}">
    <label for="title"><g:message code="event.title.label" default="Title" /></label>
    <g:textField name="title" value="${eventInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'startTime', 'error')}">
    <label for="startTime"><g:message code="event.startTime.label" default="Start Time" /></label>
    <g:textField name="startTime"
         value="${formatDate(date: occurrenceStart ? new Instant(occurrenceStart).toDate() : eventInstance?.startTime, format: 'MM/dd/yyyy hh:mm a')}"
         class="datetime" />
</div>


<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'endTime', 'error')}">
    <label for="endTime"><g:message code="event.endTime.label" default="End Time" /></label>

    <g:textField name="endTime"
         value="${formatDate(date: occurrenceEnd ? new Instant(occurrenceEnd).toDate() : eventInstance?.endTime, format: 'MM/dd/yyyy hh:mm a')}"
         class="datetime" />

        
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'location', 'error')}">
    <label for="location"><g:message code="event.location.label" default="Location" /></label>
    <g:textField name="location" value="${eventInstance?.location}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'isRecurring', 'error')}">

    <label for="isRecurring"><g:message code="event.isRecurring.label" default="Repeat" /></label>
    <g:checkBox name="isRecurring" value="${eventInstance.isRecurring}" />
    <span id="recurDescription"></span>

    <a id="editRecurringLink" ${eventInstance.isRecurring ?  "" : 'style="display:none"'} href="#">Edit</a>

</div>



<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'description', 'error')}">
    <label for="description"><g:message code="event.description.label" default="Description" /></label>
    <g:textArea name="description"  value="${eventInstance?.description}" />
</div>


<div id="recurPopup"></div>
<div id="recurOptions" style="display:none" >
    <div>
        <label>Repeats:</label>
        <g:select name="recurType" from="${EventRecurType.values()}" optionValue="name" value="${eventInstance?.recurType}"/>
    </div>

    <div>
        <label>Repeat every:</label>
        <g:select name="recurInterval" from="${1..30}" />
        <span id="repeatLabel"></span>
    </div>

    <div id="weeklyOptions" ${eventInstance.recurType != EventRecurType.WEEKLY ? 'style="display:none"' : ''}>
        <label>Repeat on: </label>
        <div class="options">
            <calendar:daysOfWeek name="recurDaysOfWeek" selectedDays="${eventInstance?.recurDaysOfWeek}" />
        </div>
    </div>

    <div><label>Ends:</label>
        <div class="input">
            <input id="recurEndOption1" name="recurEndOption" type="radio" group="recurEndOption" ${(!eventInstance.recurCount && !eventInstance.recurUntil) ? 'checked="checked"' : ''} value="never" />
            <label for="recurEndOption1">Never</label><br />

            <input id="recurEndOption2" name="recurEndOption" type="radio" group="recurEndOption" ${(eventInstance.recurCount) ? 'checked="checked"' : ''} value="occurrences" />
            <label for="recurEndOption2">After <g:textField name="recurCount" size="3" value="${eventInstance?.recurCount}" /> occurances</label><br/>

            <input id="recurEndOption3" name="recurEndOption" type="radio" group="recurEndOption" ${(eventInstance.recurUntil) ? 'checked="checked"' : ''} value="endDate" />
            <label for="recurEndOption3">On <g:textField name="recurUntil" size="8" value="${formatDate(date: eventInstance?.recurUntil, format: 'MM/dd/yyyy')}" /></label>


        </div>
    </div>

    <div>
            <strong><span id="recurSummary"></span></strong>

    </div>

</div>
