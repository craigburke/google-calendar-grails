<%@ page import="com.craigburke.Event" %>

<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'event.label', default: 'Event')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>

    <r:require module="calendar" />

</head>

<body>
<a href="#edit-event" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                            default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a href="${createLink(uri: '/')}" class="home">Home</a></li>
        <li><g:link action="index" class="calendar">Calendar</g:link></li>
        <li><g:link action="create" class="create">New Event</g:link></li>
    </ul>
</div>

<div id="edit-event" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${eventInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${eventInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form method="post">
        <g:hiddenField name="id" value="${eventInstance?.id}"/>
        <g:hiddenField name="version" value="${eventInstance?.version}"/>
        <g:hiddenField name="editType" value="" />

        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">

            <g:actionSubmit class="save ${eventInstance.isRecurring ? 'recurring' : ''}" action="update"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            <g:actionSubmit class="delete ${eventInstance.isRecurring ? 'recurring' : ''}" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" />
        </fieldset>
    </g:form>

    <g:if test="${eventInstance.isRecurring}">
        <g:render template="deletePopup" model="model" />
        <g:render template="editPopup" model="model" />
    </g:if>

</div>

</body>
</html>
