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
import com.sun.jersey.impl.provider.entity.AbstractListElementProvider;
import com.sun.jersey.impl.util.ThrowHelper;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
@Produces("application/fastinfoset")
@Consumes("application/fastinfoset")
public class FastInfosetListElementProvider extends AbstractListElementProvider {

    FastInfosetListElementProvider(@Context Providers ps) {
        super(ps, MediaTypes.FAST_INFOSET);
    }
        
    protected final XMLStreamReader getXMLStreamReader(MediaType mediaType,
            InputStream entityStream)
            throws XMLStreamException {
        return new StAXDocumentParser(entityStream);
    }
    
    protected final void writeTo(Collection<?> t, MediaType mediaType, Charset c,
            Marshaller m, OutputStream entityStream)
            throws JAXBException, IOException {
        final XMLStreamWriter xsw = new StAXDocumentSerializer(entityStream);
        for (Object o : t)
            m.marshal(o, xsw);
        
        try {
            xsw.flush();
        } catch (XMLStreamException cause) {
            throw ThrowHelper.withInitCause(cause,
                    new IOException()
                    );            
        }     
    }
}