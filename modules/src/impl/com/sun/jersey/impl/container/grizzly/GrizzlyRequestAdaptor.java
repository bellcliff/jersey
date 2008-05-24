/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.jersey.impl.container.grizzly;

import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.jersey.spi.container.AbstractContainerRequest;
import com.sun.jersey.impl.http.header.HttpHeaderFactory;
import com.sun.jersey.spi.container.MessageBodyContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Marc.Hadley@Sun.Com
 */
public final class GrizzlyRequestAdaptor  extends AbstractContainerRequest {
    
    private final GrizzlyRequest request;
    
    /** Creates a new instance of GrizzlyRequestAdaptor */
    public GrizzlyRequestAdaptor(MessageBodyContext bodyContext, GrizzlyRequest request) 
            throws IOException {
        super(bodyContext, request.getMethod(), request.getInputStream());
        this.request = request;
        
        initiateUriInfo();
        copyHttpHeaders();
    }

    private void initiateUriInfo() {
        try {
            this.baseUri = new URI(
                    request.getScheme(),
                    null,
                    request.getServerName(), 
                    request.getServerPort(),
                    "/", 
                    null, 
                    null);

            /*
             * request.unparsedURI() is a URI in encoded form that contains
             * the URI path and URI query components.
             */
            this.completeUri = baseUri.resolve(
                    request.getRequest().unparsedURI().toString());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex);
        }
    }
    
    private void copyHttpHeaders() {
        MultivaluedMap<String, String> headers = getRequestHeaders();
        
        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String)names.nextElement();
            String value = request.getHeader(name);
            headers.add(name, value);
            if (name.equalsIgnoreCase("cookie")) {
                getCookies().putAll(HttpHeaderFactory.createCookies(value));
            }
        }
    }
}
