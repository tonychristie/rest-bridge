package com.spirecentral.restbridge.dto;

import com.spirecentral.restbridge.model.RepositoryInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response from a successful connection.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectResponse {

    /**
     * Session ID for subsequent requests.
     */
    private String sessionId;

    /**
     * Information about the connected repository.
     */
    private RepositoryInfo repositoryInfo;
}
