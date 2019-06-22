package com.jasoncarloscox.familymapserver.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.jasoncarloscox.familymapserver.api.request.ApiRequest;
import com.jasoncarloscox.familymapserver.api.result.ApiResult;
import com.jasoncarloscox.familymapserver.api.result.EventResult;
import com.jasoncarloscox.familymapserver.api.result.EventsResult;
import com.jasoncarloscox.familymapserver.api.service.EventService;
import com.jasoncarloscox.familymapserver.data.access.AuthTokenAccess;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.jasoncarloscox.familymapserver.data.access.EventAccess;
import com.jasoncarloscox.familymapserver.data.model.AuthToken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jasoncarloscox.familymapserver.data.access.DatabaseTest;

public class EventServiceTest {

    private Database db;
    private AuthTokenAccess authTokenAccess;
    private EventAccess eventAccess;

    private AuthToken at;
    private ArrayList<Event> events;

    @Before
    public void setup() throws DBException {
        Database.testDBPath = DatabaseTest.TEST_DB;
        db = new Database();
        db.init();
        db.clear();
        db.commit();

        authTokenAccess = new AuthTokenAccess(db);
        eventAccess = new EventAccess(db);

        at = new AuthToken("token", "uname");
        authTokenAccess.add(at);

        events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Event e = new Event("eid" + i, at.getUsername());
            e.setPersonId("pid" + i);
            e.setLatitude(i);
            e.setLongitude(i + 1);
            e.setCountry("country");
            e.setCity("city");
            e.setType("type");
            e.setYear(2000 + i);

            events.add(e);
            eventAccess.add(e);
        }

        db.commit();
    }

    @After
    public void cleanup() throws DBException {
        db.close();
    }

    @Test
    public void getEventSucceedsIfIdFoundAndAuthTokenValid() {
        for (Event e : events) {
            EventRequest req = new EventRequest(at.getToken(), e.getId());

            ApiResult apiResult = EventService.getEvent(req);

            assertTrue(apiResult.getMessage(), apiResult.isSuccess());
            assertTrue(apiResult instanceof EventResult);

            EventResult result = (EventResult) apiResult;

            assertEquals(e.getId(), result.getId());
            assertEquals(e.getAssociatedUsername(), result.getAssociatedUsername());
            assertEquals(e.getLatitude(), result.getLatitude(), 0);
            assertEquals(e.getLongitude(), result.getLongitude(), 0);
            assertEquals(e.getCountry(), result.getCountry());
            assertEquals(e.getCity(), result.getCity());
            assertEquals(e.getType(), result.getType());
            assertEquals(e.getYear(), result.getYear());
        }
    }

    @Test
    public void getEventFailsIfIdNotFound() {
        EventRequest req = new EventRequest(at.getToken(), "invalid");

        ApiResult result = EventService.getEvent(req);

        assertFalse(result.getMessage(), result.isSuccess());
    }

    @Test
    public void getEventFailsIfAuthTokenNotValid() {
        EventRequest req = new EventRequest("invalid", events.get(0).getId());

        ApiResult result = EventService.getEvent(req);

        assertFalse(result.getMessage(), result.isSuccess());
    }

    @Test
    public void getEventsSucceedsIfAuthTokenValid() {
        ApiRequest req = new ApiRequest(at.getToken());

        EventsResult result = EventService.getEvents(req);

        assertTrue(result.getMessage(), result.isSuccess());

        assertEquals(events.size(), result.getData().size());
    }

    @Test
    public void getEventsFailsIfAuthTokenNotValid() {
        ApiRequest req = new ApiRequest("invalid");

        EventsResult result = EventService.getEvents(req);

        assertFalse(result.getMessage(), result.isSuccess());
    }

}