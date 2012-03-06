<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">

  <title>Calendar</title>
  <r:require module="calendar" />
</head>
<body>
<div class="nav" role="navigation">
    <ul>
        <li><a href="${createLink(uri: '/')}" class="home">Home</a></li>
        <li><g:link action="index" class="calendar">Calendar</g:link></li>
        <li><g:link action="create" class="create">New Event</g:link></li>
    </ul>
</div>

    <div id="calendar"></div>

</body>
</html>