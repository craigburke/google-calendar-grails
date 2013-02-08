<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
        <r:require module="home" />
	</head>
	<body>
    <div class="nav" role="navigation">
        <ul>
            <li><a href="${createLink(uri: '/')}" class="home">Home</a></li>
            <li><g:link class="calendar" controller="event" action="index">Calendar</g:link></li>
            <li><g:link class="create" controller="event" action="create">New Event</g:link></li>
        </ul>
    </div>

		<div id="page-body" role="main">
			<h1>Google Calendar Grails Demo</h1>
			
            <h2>See the calendar in action</h2>
            <p><g:link action="index" controller="event" >Calendar Demo</g:link>
            
            <h2>Read the blog</h2>
            <p>
            I detail how I created this calendar on my blog:
                <ul>
                    <li><a href="http://www.craigburke.com/blog/2012/02/09/creating-google-calendar-in-grails-part-1-the-model/">Part 1: The Model</a></li>
                    <li><a href="http://www.craigburke.com/blog/2012/02/16/creating-google-calendar-in-grails-part-2-displaying-the-calendar/">Part 2: Displaying the Calendar</a></li>
                    <li><a href="http://www.craigburke.com/blog/2012/02/18/creating-google-calendar-in-grails-part-3-creating-and-modifying-events/">Part 3: Creating and Modifying Events</a></li>
                </ul>    
                
            </p>
            

            <h2>Check out the source</h2>
            <p>
                <a href="https://github.com/craigburke/google-calendar-grails">Google Calendar Grails on Github</a>

            </p>
            </div>
	</body>
</html>
