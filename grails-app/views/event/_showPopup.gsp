<%@ page import="org.joda.time.Instant" %>

<div class="eventPopup">

<h2>${eventInstance.title}</h2>
<p class="date">
    <g:formatDate date="${new Instant(startTime).toDate()}" format="E, MMM d, hh:mma"/>  –
    <g:formatDate date="${new Instant(endTime).toDate()}" format="E, MMM d, hh:mma"/>
</p>
<p>
    <g:link action="show" id="${eventInstance.id}" params="[startTime: startTime, endTime: endTime]">More details »</g:link>
</p>
</div>