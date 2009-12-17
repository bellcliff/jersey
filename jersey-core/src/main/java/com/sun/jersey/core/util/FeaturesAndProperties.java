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
package com.sun.jersey.core.util;

import java.util.Map;

/**
 * Features and properties.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public interface FeaturesAndProperties {
    /**
     * If true XML security features when parsing XML documents will be
     * disabled.
     * <p>
     * The default value is false.
     */
    public static final String FEATURE_DISABLE_XML_SECURITY
            = "com.sun.jersey.config.feature.DisableXmlSecurity";

    /**
     * If true then returned XML will be formatted.
     * <p>
     * Formatting is done by setting Marshaller.JAXB_FORMATTED_OUTPUT when
     * Marshallers are created. This will cause that produced XML will be
     * formatted (line breaks and indentation).
     * <p>
     * The default value is false.
     */
    public static final String FEATURE_FORMATTED
            = "com.sun.jersey.config.feature.Formatted";

    /**
     * Get the map of features associated with the client.
     *
     * @return the features.
     *         The returned value shall never be null.
     */
    Map<String, Boolean> getFeatures();

    /**
     * Get the value of a feature.
     *
     * @param featureName the feature name.
     * @return true if the feature is present and set to true, otherwise false
     *         if the feature is present and set to false or the feature is not
     *         present.
     */
    boolean getFeature(String featureName);

    /**
     * Get the map of properties associated with the client.
     *
     * @return the properties.
     *         The returned value shall never be null.
     */
    Map<String, Object> getProperties();

    /**
     * Get the value of a property.
     *
     * @param propertyName the property name.
     * @return the property, or null if there is no property present for the
     *         given property name.
     */
    Object getProperty(String propertyName);
}
