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

package com.sun.jersey.core.header;

import com.sun.jersey.core.header.reader.HttpHeaderReader;
import java.text.ParseException;
import java.util.Date;

/**
 * A form-data content disposition header.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public class FormDataContentDisposition extends ContentDisposition {
    private String name;

    /**
     * Constructor for the builder.
     *
     * @param type the disposition type. will be "form-data".
     * @param name the control name.
     * @param fileName the file name.
     * @param creationDate the creation date.
     * @param modificationDate the modification date.
     * @param readDate the read date.
     * @param size the size.
     */
    protected FormDataContentDisposition(String type, String name, String fileName,
            Date creationDate, Date modificationDate, Date readDate,
            long size) {
        super(type, fileName, creationDate, modificationDate, readDate, size);
        this.name = name;
    }

    public FormDataContentDisposition(String header) throws ParseException {
        this(HttpHeaderReader.newInstance(header));
    }

    public FormDataContentDisposition(HttpHeaderReader reader) throws ParseException {
        super(reader);
        if (!getType().equals("form-data")) {
            throw new IllegalArgumentException("The content dispostion type is not equal to form-data");
        }

        name = getParameters().get("name");
        if (name == null) {
            throw new IllegalArgumentException("The name parameter is not present");
        }
    }

    /**
     * Get the name parameter.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    protected StringBuilder toStringBuffer() {
        StringBuilder sb = super.toStringBuffer();

        addStringParameter(sb, "name", name);

        return sb;
    }

    /**
     * Start building a form data content disposition.
     *
     * @param name the control name.
     * @return the form data content disposition builder.
     */
    public static FormDataContentDispositionBuilder name(String name) {
        return new FormDataContentDispositionBuilder(name);
    }

    /**
     * Builder to build form data content disposition.
     * 
     */
    public static class FormDataContentDispositionBuilder extends ContentDispositionBuilder<FormDataContentDispositionBuilder, FormDataContentDisposition> {
        private String name;

        FormDataContentDispositionBuilder(String name) {
            super("form-data");
            this.name = name;
        }

        @Override
        public FormDataContentDisposition build() {
            FormDataContentDisposition cd = new FormDataContentDisposition(type, name, fileName, creationDate, modificationDate, readDate, size);
            return cd;
        }
    }
}