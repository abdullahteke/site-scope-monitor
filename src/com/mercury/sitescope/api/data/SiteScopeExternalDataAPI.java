/**
 * SiteScopeExternalDataAPI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mercury.sitescope.api.data;

public interface SiteScopeExternalDataAPI extends javax.xml.rpc.Service {
    public java.lang.String getAPIDataAcquisitionImplAddress();

    public com.mercury.sitescope.api.data.IAPIDataAcquisition getAPIDataAcquisitionImpl() throws javax.xml.rpc.ServiceException;

    public com.mercury.sitescope.api.data.IAPIDataAcquisition getAPIDataAcquisitionImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
