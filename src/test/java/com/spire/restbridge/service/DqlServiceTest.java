package com.spire.restbridge.service;

import com.spire.restbridge.dto.DqlRequest;
import com.spire.restbridge.exception.SessionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DqlService.
 */
class DqlServiceTest {

    @Mock
    private SessionService sessionService;

    private DqlService dqlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dqlService = new DqlService(sessionService);
    }

    @Test
    void executeQuery_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
                .thenThrow(new SessionNotFoundException("invalid-session"));

        DqlRequest request = new DqlRequest();
        request.setSessionId("invalid-session");
        request.setQuery("select * from dm_document");

        assertThrows(SessionNotFoundException.class, () -> {
            dqlService.executeQuery(request);
        });
    }

    @Test
    void isDqlAvailable_invalidSession_throwsException() {
        when(sessionService.getSession("invalid-session"))
                .thenThrow(new SessionNotFoundException("invalid-session"));

        assertThrows(SessionNotFoundException.class, () -> {
            dqlService.isDqlAvailable("invalid-session");
        });
    }
}
