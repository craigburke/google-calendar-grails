<%@ page import="com.craigburke.Event" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'event.label', default: 'Event')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-event" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="show-event" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list event">

        <g:if test="${eventInstance?.title}">
            <li class="fieldcontain">
                <span id="title-label" class="property-label"><g:message code="event.title.label"
                                                                         default="Title"/></span>

                <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${eventInstance}"
                                                                                         field="title"/></span>

            </li>
        </g:if>

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

        <g:if test="${eventInstance?.recurType}">
            <li class="fieldcontain">
                <span id="recurType-label" class="property-label"><g:message code="event.recurType.label"
                                                                             default="Recur Type"/></span>

                <span class="property-value" aria-labelledby="recurType-label"><g:fieldValue bean="${eventInstance}"
                                                                                             field="recurType"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.recurInterval}">
            <li class="fieldcontain">
                <span id="recurInterval-label" class="property-label"><g:message code="event.recurInterval.label"
                                                                                 default="Recur Interval"/></span>

                <span class="property-value" aria-labelledby="recurInterval-label"><g:fieldValue bean="${eventInstance}"
                                                                                                 field="recurInterval"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.recurUntil}">
            <li class="fieldcontain">
                <span id="recurUntil-label" class="property-label"><g:message code="event.recurUntil.label"
                                                                              default="Recur Until"/></span>

                <span class="property-value" aria-labelledby="recurUntil-label"><g:formatDate
                        date="${eventInstance?.recurUntil}"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.recurCount}">
            <li class="fieldcontain">
                <span id="recurCount-label" class="property-label"><g:message code="event.recurCount.label"
                                                                              default="Recur Count"/></span>

                <span class="property-value" aria-labelledby="recurCount-label"><g:fieldValue bean="${eventInstance}"
                                                                                              field="recurCount"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.startTime}">
            <li class="fieldcontain">
                <span id="startTime-label" class="property-label"><g:message code="event.startTime.label"
                                                                             default="Start Time"/></span>

                <span class="property-value" aria-labelledby="startTime-label"><g:formatDate
                        date="${eventInstance?.startTime}"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.excludeDays}">
            <li class="fieldcontain">
                <span id="excludeDays-label" class="property-label"><g:message code="event.excludeDays.label"
                                                                               default="Exclude Days"/></span>

                <span class="property-value" aria-labelledby="excludeDays-label"><g:fieldValue bean="${eventInstance}"
                                                                                               field="excludeDays"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.recurDaysOfWeek}">
            <li class="fieldcontain">
                <span id="recurDaysOfWeek-label" class="property-label"><g:message code="event.recurDaysOfWeek.label"
                                                                                   default="Recur Days Of Week"/></span>

                <span class="property-value" aria-labelledby="recurDaysOfWeek-label"><g:fieldValue
                        bean="${eventInstance}" field="recurDaysOfWeek"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.endTime}">
            <li class="fieldcontain">
                <span id="endTime-label" class="property-label"><g:message code="event.endTime.label"
                                                                           default="End Time"/></span>

                <span class="property-value" aria-labelledby="endTime-label"><g:formatDate
                        date="${eventInstance?.endTime}"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.isRecurring}">
            <li class="fieldcontain">
                <span id="isRecurring-label" class="property-label"><g:message code="event.isRecurring.label"
                                                                               default="Is Recurring"/></span>

                <span class="property-value" aria-labelledby="isRecurring-label"><g:formatBoolean
                        boolean="${eventInstance?.isRecurring}"/></span>

            </li>
        </g:if>

        <g:if test="${eventInstance?.sourceEvent}">
            <li class="fieldcontain">
                <span id="sourceEvent-label" class="property-label"><g:message code="event.sourceEvent.label"
                                                                               default="Source Event"/></span>

                <span class="property-value" aria-labelledby="sourceEvent-label"><g:link controller="event"
                                                                                         action="show"
                                                                                         id="${eventInstance?.sourceEvent?.id}">${eventInstance?.sourceEvent?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

    </ol>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${eventInstance?.id}"/>
            <g:link class="edit" action="edit" id="${eventInstance?.id}"><g:message code="default.button.edit.label"
                                                                                    default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
