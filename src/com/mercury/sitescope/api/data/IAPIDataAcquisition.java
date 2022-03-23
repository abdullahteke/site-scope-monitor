/**
 * IAPIDataAcquisition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.mercury.sitescope.api.data;

public interface IAPIDataAcquisition extends java.rmi.Remote {
    public byte[] getData(java.lang.String[] in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.mercury.sitescope.api.configuration.exception.ExternalServiceAPIException;
    public byte[] getDataWithTopology(java.lang.String[] in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.mercury.sitescope.api.configuration.exception.ExternalServiceAPIException;
    public byte[] getMonitorTypesWithMetricNames(boolean in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.mercury.sitescope.api.configuration.exception.ExternalServiceAPIException;
}
