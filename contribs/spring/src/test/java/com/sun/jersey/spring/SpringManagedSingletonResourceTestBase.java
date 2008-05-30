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
package com.sun.jersey.spring;

import org.testng.Assert;

import com.sun.jersey.api.client.WebResource;

/**
 * Test spring managed singleton resources having at least one constructor.<br>
 * Created on: Apr 10, 2008<br>
 * 
 * @author <a href="mailto:martin.grotzke@freiheit.com">Martin Grotzke</a>
 * @version $Id$
 */
public class SpringManagedSingletonResourceTestBase extends AbstractResourceTest {
    
    private final String _resourcePath;

    public SpringManagedSingletonResourceTestBase( String resourcePath ) {
        _resourcePath = resourcePath;
    }
    
    public void testGetManagedName() {

        final WebResource rootResource = resource( _resourcePath );
        
        final String actual = rootResource.get( String.class );
        Assert.assertNotNull( actual );
        Assert.assertEquals( actual, TestData.MANAGED );
        
    }
    
    public void testGetAndUpdateManagedItem() {
        
        final WebResource itemResource = resource( _resourcePath + "/item" );
        final Item actualItem = itemResource.get( Item.class );
        Assert.assertNotNull( actualItem );
        Assert.assertEquals( actualItem.getValue(), TestData.MANAGED );

        /* update the value of the singleton item and afterwards check if it's the same
         */
        final String newValue = "newValue";
        final WebResource itemValueResource = resource( _resourcePath + "/item/value/" + newValue );
        itemValueResource.put();
        
        final Item actualUpdatedItem = itemResource.get( Item.class );
        Assert.assertNotNull( actualUpdatedItem );
        Assert.assertEquals( actualUpdatedItem.getValue(), newValue );
        
    }
    
    public void testGetAndUpdateCount() {
        
        final WebResource countResource = resource( _resourcePath + "/countusage" );
        
        final int actualCount = Integer.parseInt( countResource.get( String.class ) );
        countResource.post();
        final int actualCountUpdated = Integer.parseInt( countResource.get( String.class ) );
        Assert.assertEquals( actualCountUpdated, actualCount + 1 );
        
    }

}