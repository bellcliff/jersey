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

package com.sun.jersey.impl.provider.entity.fastinfoset;

import com.sun.jersey.api.MediaTypes;
import com.sun.jersey.impl.ImplMessages;
import com.sun.jersey.impl.provider.entity.AbstractJAXBElementProvider;
import com.sun.jersey.impl.util.ThrowHelper;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
@Produces("application/fastinfoset")
@Consumes("application/fastinfoset")
public final class FastInfosetJAXBElementProvider extends AbstractJAXBElementProvider {
    
    public FastInfosetJAXBElementProvider(@Context Providers ps) {
        super(ps, MediaTypes.FAST_INFOSET);
    }
    
    @SuppressWarnings("unchecked")
    public JAXBElement<?> readFrom(
            Class<JAXBElement<?>> type, 
            Type genericType, 
            Annotation annotations[],
            MediaType mediaType, 
            MultivaluedMap<String, String> httpHeaders, 
            InputStream entityStream) throws IOException {
        final ParameterizedType pt = (ParameterizedType)genericType;
        final Class ta = (Class)pt.getActualTypeArguments()[0];
        
        try {
            final StAXDocumentParser p = new StAXDocumentParser(entityStream);
            return getUnmarshaller(ta, mediaType).unmarshal(p, ta);
        } catch (JAXBException cause) {
            cause.printStackTrace();
            throw ThrowHelper.withInitCause(cause,
                    new IOException(ImplMessages.ERROR_UNMARSHALLING_JAXB(type))
                    );
        }    
        
    }
    
    public void writeTo(
            JAXBElement<?> t, 
            Class<?> type, 
            Type genericType, 
            Annotation annotations[], 
            MediaType mediaType, 
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException {
        try {
            final Marshaller marshaller = getMarshaller(t.getDeclaredType(), mediaType);
            final String name = getCharsetAsString(mediaType);
            if (name != null) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, name);
            }
            final XMLStreamWriter xsw = new StAXDocumentSerializer(entityStream);
            marshaller.marshal(t, xsw);
        } catch (JAXBException cause) {
            throw ThrowHelper.withInitCause(cause,
                    new IOException(ImplMessages.ERROR_MARSHALLING_JAXB(t.getClass()))
                    );
        }
    }
}