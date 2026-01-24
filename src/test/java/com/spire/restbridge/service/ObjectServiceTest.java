package com.spire.restbridge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spire.restbridge.dto.CreateObjectRequest;
import com.spire.restbridge.exception.ObjectNotFoundException;
import com.spire.restbridge.exception.SessionNotFoundException;
import com.spire.restbridge.model.ObjectInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

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
