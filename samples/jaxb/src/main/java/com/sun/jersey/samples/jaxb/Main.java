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
package com.sun.jersey.samples.jaxb;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class Main {
    
    final static String BASE_URI = "http://localhost:9998/";
   
    public static SelectorThread startServer() throws IOException {
        final Map<String, String> initParams = new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages",
                "com.sun.jersey.samples.jaxb");

        SelectorThread threadSelector = GrizzlyWebContainerFactory.create(BASE_URI, initParams);
        return threadSelector;
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("Starting grizzly...");
        SelectorThread threadSelector = startServer();
        System.out.println(String.format(
                "Jersey app started with WADL available at %s/application.wadl\n" +
                "Hit enter to stop it...", BASE_URI));
        System.in.read();
        threadSelector.stopEndpoint();
        System.exit(0);
    }    
}
