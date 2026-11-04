package com.spire.restbridge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spire.restbridge.dto.CreateObjectRequest;
import com.spire.restbridge.dto.UpdateObjectRequest;
import com.spire.restbridge.exception.ObjectNotFoundException;
import com.spire.restbridge.exception.RestBridgeException;
import com.spire.restbridge.exception.SessionNotFoundException;
import com.spire.restbridge.model.ObjectInfo;
import com.spire.restbridge.model.TypeInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ObjectService.
 */
class ObjectServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private TypeService typeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ObjectService objectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectService = new ObjectService(sessionService, typeService, objectMapper);
    }

    @Test
    void getObject_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.getObject("invalid-session", "0901234567890001");
        });
    }

    @Test
    void getCabinets_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.getCabinets("invalid-session");
        });
    }

    @Test
    void listFolderContentsById_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.listFolderContentsById("invalid-session", "0b01234567890001");
        });
    }

    @Test
    void updateObject_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.updateObject("invalid-session", "0901234567890001", null);
        });
    }

    @Test
    void updateObject_wrapsScalarForRepeatingAttributeOfLookedUpType() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(jsonResponse("{\"properties\": {\"r_object_type\": \"my_doc\"}}"));
            server.enqueue(jsonResponse("{\"operations\": [{\"id\": \"updateObject\", \"response\": "
                    + "{\"status\": 200, \"entity\": \"{\\\"properties\\\": "
                    + "{\\\"r_object_id\\\": \\\"0901234567890001\\\", \\\"r_object_type\\\": \\\"my_doc\\\"}}\"}}]}"));
            server.start();
            mockSession(server);
            when(typeService.getTypeInfo("session-1", "my_doc")).thenReturn(TypeInfo.builder()
                    .name("my_doc")
                    .attributes(List.of(TypeInfo.AttributeInfo.builder()
                            .name("keywords").dataType("STRING").repeating(true).build()))
                    .build());

            objectService.updateObject("session-1", "0901234567890001", updateRequest());

            RecordedRequest typeLookup = server.takeRequest();
            assertTrue(typeLookup.getPath().contains("view=r_object_type"));
            String batchBody = server.takeRequest().getBody().readUtf8();
            assertTrue(batchBody.contains("\\\"keywords\\\":[\\\"alpha\\\"]"));
        }
    }

    @Test
    void updateObject_typeLookupFails_throwsAndSendsNoUpdate() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setResponseCode(500));
            server.start();
            mockSession(server);

            assertThrows(RestBridgeException.class, () ->
                    objectService.updateObject("session-1", "0901234567890001", updateRequest()));
            assertEquals(1, server.getRequestCount());
        }
    }

    @Test
    void updateObject_typeLookupNotFound_throwsObjectNotFound() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setResponseCode(404));
            server.start();
            mockSession(server);

            assertThrows(ObjectNotFoundException.class, () ->
                    objectService.updateObject("session-1", "0901234567890001", updateRequest()));
            assertEquals(1, server.getRequestCount());
        }
    }

    private void mockSession(MockWebServer server) {
        RestSessionHolder session = new RestSessionHolder();
        session.setRepository("testrepo");
        session.setWebClient(WebClient.create(server.url("/").toString()));
        when(sessionService.getSession("session-1")).thenReturn(session);
    }

    private static MockResponse jsonResponse(String body) {
        return new MockResponse().setHeader("Content-Type", "application/json").setBody(body);
    }

    private static UpdateObjectRequest updateRequest() {
        return UpdateObjectRequest.builder()
                .sessionId("session-1")
                .attributes(Map.of("keywords", "alpha"))
                .build();
    }

    @Test
    void checkout_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.checkout("invalid-session", "0901234567890001");
        });
    }

    @Test
    void cancelCheckout_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.cancelCheckout("invalid-session", "0901234567890001");
        });
    }

    @Test
    void checkin_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.checkin("invalid-session", "0901234567890001", "CURRENT");
        });
    }

    @Test
    void createObject_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        CreateObjectRequest request = CreateObjectRequest.builder()
            .sessionId("invalid-session")
            .objectType("dm_document")
            .objectName("test.txt")
            .folderPath("/Temp")
            .build();

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.createObject("invalid-session", request);
        });
    }

    @Test
    void deleteObject_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
            .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            objectService.deleteObject("invalid-session", "0901234567890001", false);
        });
    }
}
