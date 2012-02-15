package com.craigburke



import org.junit.*
import grails.test.mixin.*

@TestFor(EventController)
@Mock(Event)
class EventControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/event/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.eventInstanceList.size() == 0
        assert model.eventInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.eventInstance != null
    }

    void testSave() {
        controller.save()

        assert model.eventInstance != null
        assert view == '/event/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/event/show/1'
        assert controller.flash.message != null
        assert Event.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/event/list'


        populateValidParams(params)
        def event = new Event(params)

        assert event.save() != null

        params.id = event.id

        def model = controller.show()

        assert model.eventInstance == event
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/event/list'


        populateValidParams(params)
        def event = new Event(params)

        assert event.save() != null

        params.id = event.id

        def model = controller.edit()

        assert model.eventInstance == event
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/event/list'

        response.reset()


        populateValidParams(params)
        def event = new Event(params)

        assert event.save() != null

        // test invalid parameters in update
        params.id = event.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/event/edit"
        assert model.eventInstance != null

        event.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/event/show/$event.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        event.clearErrors()

        populateValidParams(params)
        params.id = event.id
        params.version = -1
        controller.update()

        assert view == "/event/edit"
        assert model.eventInstance != null
        assert model.eventInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/event/list'

        response.reset()

        populateValidParams(params)
        def event = new Event(params)

        assert event.save() != null
        assert Event.count() == 1

        params.id = event.id

        controller.delete()

        assert Event.count() == 0
        assert Event.get(event.id) == null
        assert response.redirectedUrl == '/event/list'
    }
}
