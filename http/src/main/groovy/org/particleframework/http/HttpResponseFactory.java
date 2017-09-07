/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.http;

import org.particleframework.core.io.service.SoftServiceLoader;
import org.particleframework.http.cookie.CookieFactory;

/**
 * @author Graeme Rocher
 * @since 1.0
 */
public interface HttpResponseFactory {

    /**
     * The default {@link CookieFactory} instance
     */
    HttpResponseFactory INSTANCE = SoftServiceLoader.load(HttpResponseFactory.class)
            .first()
            .map(SoftServiceLoader.Service::load)
            .orElse(null);

    /**
     * @return The ok response
     */
    MutableHttpResponse ok();

    /**
     * @param status The status
     * @return The restus response
     */
    MutableHttpResponse status(HttpStatus status);

    /**
     * Creates an {@link HttpStatus#OK} response with a body
     *
     * @param body The body
     * @param <T> The body type
     * @return The ok response with the given body
     */
    <T> MutableHttpResponse<T> ok(T body);
}
