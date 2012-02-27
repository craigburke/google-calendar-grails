<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>Welcome to Grails</title>
        <r:require module="home" />
	</head>
	<body>
		<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div id="status" role="complementary">
			<h1>Application Status</h1>
			<ul>
				<li>App version: <g:meta name="app.version"/></li>
				<li>Grails version: <g:meta name="app.grails.version"/></li>
				<li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
				<li>JVM version: ${System.getProperty('java.version')}</li>
				<li>Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</li>
				<li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
				<li>Domains: ${grailsApplication.domainClasses.size()}</li>
				<li>Services: ${grailsApplication.serviceClasses.size()}</li>
				<li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
			</ul>
			<h1>Installed Plugins</h1>
			<ul>
				<g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
					<li>${plugin.name} - ${plugin.version}</li>
				</g:each>
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
                    <li>Part 3: Coming Soon</li>
                </ul>    
                
            </p>
            

            <h2>Check out the source</h2>
            <p>
                <a href="https://github.com/craigburke/google-calendar-grails">Google Calendar Grails on Github</a>

            </p>
	</body>
</html>
