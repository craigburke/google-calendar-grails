<%@ page import="com.craigburke.Event" %>
<%@ page import="org.joda.time.Instant" %>


<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'event.label', default: 'Event')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>

    <r:require module="calendar" />

</head>

<body>
<a href="#show-event" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a href="${createLink(uri: '/')}" class="home">Home</a></li>
        <li><g:link action="index" class="calendar">Calendar</g:link></li>
        <li><g:link action="create" class="create">New Event</g:link></li>
    </ul>
</div>

<div id="show-event" class="content scaffold-show" role="main">
    <h1>${eventInstance?.title}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <ol class="property-list event">
        <li class="fieldcontain">
            <span id="when-label" class="property-label">When</span>

            <span class="property-value" aria-labelledby="when-label">
                <g:formatDate date="${new Instant(occurrenceStart).toDate()}" format="E, MMM d, hh:mma"/>  –
                <g:formatDate date="${new Instant(occurrenceEnd).toDate()}" format="E, MMM d, hh:mma"/>
            </span>

        </li>

        <g:if test="${eventInstance?.location}">
            <li class="fieldcontain">
                <span id="location-label" class="property-label"><g:message code="event.location.label"
                                                                            default="Location"/></span>

                <span class="property-value" aria-labelledby="location-label"><g:fieldValue bean="${eventInstance}"
                                                                                            field="location"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.description}">
            <li class="fieldcontain">
                <span id="description-label" class="property-label"><g:message code="event.description.label"
                                                                               default="Description"/></span>

                <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${eventInstance}"
                                                                                               field="description"/></span>

            </li>
        </g:if>



    </ol>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${eventInstance?.id}"/>

            <g:hiddenField name="occurrenceStart" value="${occurrenceStart}" />
            <g:hiddenField name="occurrenceEnd" value="${occurrenceEnd}" />

            <g:actionSubmit class="edit" action="edit"
                            value="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
            <g:actionSubmit class="delete ${eventInstance.isRecurring ? 'recurring' : ''}" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}" />
        </fieldset>
    </g:form>

    <g:if test="${eventInstance.isRecurring}">
        <g:render template="deletePopup" model="model" />
    </g:if>

</div>
</body>
</html>
