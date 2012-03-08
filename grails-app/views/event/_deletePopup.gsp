<div id="deletePopup" style="display: none;">

<g:form action="delete">
    <g:hiddenField name="id" value="${eventInstance.id}" />
    <g:hiddenField name="occurrenceStart" value="${occurrenceStart}" />

    <p>Would you like to delete only this event, or all events in the series?</p>

    <table>
        <tbody>
        <tr>
            <td><button type="submit" name="deleteType" value="occurrence">Only this event</button></td>
            <td>All other events in the series will remain the same.</td>
        </tr>

        <tr>
            <td><button type="submit" name="deleteType" value="following">All following</button></td>
            <td>This and all the following events will be changed.</td>
        </tr>
        <tr>
            <td><button type="submit" name="deleteType" value="all">All events in the series</button></td>
            <td>All other events in the series will remain the same.</td>
        </tr>
        </tbody>

    </table>

</div>


</g:form>

</div>

