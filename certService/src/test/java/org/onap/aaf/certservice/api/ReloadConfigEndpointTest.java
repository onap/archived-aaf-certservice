/*
 * ============LICENSE_START=======================================================
 * PROJECT
 * ================================================================================
 * Copyright (C) 2020 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.aaf.certservice.api;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.aaf.certservice.certification.configuration.CmpServersConfig;
import org.onap.aaf.certservice.certification.configuration.CmpServersConfig.LoadingStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ReloadConfigEndpointTest {

    private ReloadConfigEndpoint reloadConfigEndpoint;

    @Mock
    public CmpServersConfig cmpServersConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.reloadConfigEndpoint = new ReloadConfigEndpoint(cmpServersConfig);
    }

    @Test
    public void shouldReturnStatusOkWhenSuccessfullyReloaded() {
        // Given
        LoadingStatus loadingStatus = new LoadingStatus(true);
        when(cmpServersConfig.reloadConfiguration()).thenReturn(loadingStatus);

        // When
        ResponseEntity<String> response = reloadConfigEndpoint.reload();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnStatusErrorWhenReloadFailed() {
        // Given
        LoadingStatus loading = new LoadingStatus(false);
        when(cmpServersConfig.reloadConfiguration()).thenReturn(loading);

        // When
        ResponseEntity<String> response = reloadConfigEndpoint.reload();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldReturnErrorMessageWhenReloadFailed() {
        // Given
        String ERROR_MESSAGE = "Error Message";
        LoadingStatus loading = new LoadingStatus(false, ERROR_MESSAGE);
        when(cmpServersConfig.reloadConfiguration()).thenReturn(loading);

        // When
        ResponseEntity<String> response = reloadConfigEndpoint.reload();

        // Then
        assertThat(response.getBody()).isEqualTo(ERROR_MESSAGE);
    }
}
