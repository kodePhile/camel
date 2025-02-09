/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.telegram;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.DefaultComponent;

@Component("telegram")
public class TelegramComponent extends DefaultComponent {

    @Metadata(label = "security", secret = true)
    private String authorizationToken;

    public TelegramComponent() {
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        TelegramConfiguration configuration = new TelegramConfiguration();

        // ignore trailing slash
        if (remaining.endsWith("/")) {
            remaining = remaining.substring(0, remaining.length() - 1);
        }
        configuration.setType(remaining);

        if (!remaining.equals(TelegramConfiguration.ENDPOINT_TYPE_BOTS)) {
            throw new IllegalArgumentException("Unsupported endpoint type for uri " + uri + ", remaining " + remaining);
        }

        TelegramEndpoint endpoint = new TelegramEndpoint(uri, this, configuration);
        configuration.setAuthorizationToken(authorizationToken);
        setProperties(endpoint, parameters);

        if (endpoint.getConfiguration().getAuthorizationToken() == null) {
            throw new IllegalArgumentException("AuthorizationToken must be configured on either component or endpoint for telegram: " + uri);
        }

        return endpoint;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    /**
     * The default Telegram authorization token to be used when the information is not provided in the endpoints.
     */
    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

}
