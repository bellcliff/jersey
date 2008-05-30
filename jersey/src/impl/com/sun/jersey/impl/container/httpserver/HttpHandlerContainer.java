/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.jersey.impl.container.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.HttpResponseContext;
import com.sun.jersey.spi.container.ContainerListener;
import com.sun.jersey.spi.container.WebApplication;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.core.Response;

/**
 * A {@link HttpHandler} for a {@link WebApplicationImpl}.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public class HttpHandlerContainer implements HttpHandler, ContainerListener {
    
    private WebApplication application;
    
    public HttpHandlerContainer(WebApplication app) throws ContainerException {
        this.application = app;
    }
    
    public void handle(HttpExchange httpExchange) throws IOException {
        WebApplication _application = application;
        
        HttpServerRequestAdaptor requestAdaptor = 
                new HttpServerRequestAdaptor(
                _application.getMessageBodyContext(), 
                httpExchange);
        HttpServerResponseAdaptor responseAdaptor = 
                new HttpServerResponseAdaptor(httpExchange,
                _application.getMessageBodyContext(), 
                requestAdaptor);
        
        try {
            _application.handleRequest(requestAdaptor, responseAdaptor);
        } catch (ContainerException e) {
            onException(e, responseAdaptor);
        } catch (RuntimeException e) {
            // Unexpected error associated with the runtime
            // This is a bug
            onException(e, responseAdaptor);            
        }
        
        try {
            responseAdaptor.commitAll();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    private static void onException(Exception e, HttpResponseContext response) {
        // Log the stack trace
        e.printStackTrace();

        // Write out the exception to a string
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();

        response.setResponse(Response.serverError().
                entity(sw.toString()).type("text/plain").build());
    }

    // ContainerListener
    
    public void onReload() {
        application = application.clone();
    }
}