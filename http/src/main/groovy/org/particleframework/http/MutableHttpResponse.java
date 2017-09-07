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

import org.particleframework.http.cookie.Cookie;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * @author Graeme Rocher
 * @since 1.0
 */
public interface MutableHttpResponse<B> extends HttpResponse<B> {
    /**
     * Adds the specified cookie to the response.  This method can be called
     * multiple times to set more than one cookie.
     *
     * @param cookie the Cookie to return to the client
     *
     */
    MutableHttpResponse<B> addCookie(Cookie cookie);

    /**
     * Sets the response encoding. Should be called after {@link #setContentType(MediaType)}
     *
     * @param encoding The encoding to use
     */
    default MutableHttpResponse<B> setCharacterEncoding(CharSequence encoding) {
        if(encoding != null) {
            MediaType mediaType = getContentType();
            if(mediaType != null) {
                setContentType(new MediaType(mediaType.toString(), Collections.singletonMap(MediaType.CHARSET_PARAMETER, encoding.toString())));
            }
        }
        return this;
    }

    /**
     * Sets the response encoding
     *
     * @param encoding The encoding to use
     */
    default MutableHttpResponse<B> setCharacterEncoding(Charset encoding) {
        return setCharacterEncoding(encoding.toString());
    }

    /**
     * Sets the response status
     *
     * @param status The status
     */
    MutableHttpResponse<B> setStatus(HttpStatus status, CharSequence message);
    /**
     * Set a response header
     *
     * @param name The name of the header
     * @param value The value of the header
     */
    default MutableHttpResponse<B> addHeader(CharSequence name, CharSequence value) {
        getHeaders().add(name, value);
        return this;
    }

    /**
     * Set multiple headers
     *
     * @param namesAndValues The names and values
     */
    default MutableHttpResponse<B> addHeaders(Map<CharSequence,CharSequence> namesAndValues) {
        MutableHttpHeaders headers = getHeaders();
        namesAndValues.forEach(headers::add);
        return this;
    }

    /**
     * Sets the content length
     *
     * @param length The length
     * @return This HttpResponse
     */
    default MutableHttpResponse<B> setContentLength(long length) {
        getHeaders().add(HttpHeaders.CONTENT_LENGTH, String.valueOf(length));
        return this;
    }

    /**
     * Set the response content type
     *
     * @param contentType The content type
     */
    default MutableHttpResponse<B> setContentType(CharSequence contentType) {
        getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        return this;
    }

    /**
     * Set the response content type
     *
     * @param mediaType The media type
     */
    default MutableHttpResponse<B> setContentType(MediaType mediaType) {
        getHeaders().add(HttpHeaders.CONTENT_TYPE, mediaType);
        return this;
    }
    /**
     * Sets the locale to use and will apply the appropriate {@link HttpHeaders#CONTENT_LANGUAGE} header to the response
     *
     * @param locale The locale
     * @return This response
     */
    default MutableHttpResponse<B> setLocale(Locale locale) {
        getHeaders().add(HttpHeaders.CONTENT_TYPE, locale.toString());
        return this;
    }

    /**
     * Sets the response status
     *
     * @param status The status
     */
    default MutableHttpResponse<B> setStatus(int status) {
        return setStatus(HttpStatus.valueOf(status));
    }

    /**
     * Sets the response status
     *
     * @param status The status
     */
    default MutableHttpResponse<B> setStatus(int status, CharSequence message) {
        return setStatus(HttpStatus.valueOf(status), message);
    }

    /**
     * Sets the response status
     *
     * @param status The status
     */
    default MutableHttpResponse<B> setStatus(HttpStatus status) {
        return setStatus(status, null);
    }


    /**
     * Sets the body
     *
     * @param body The body
     * @return This response object
     */
    MutableHttpResponse<B> setBody(B body);
}
