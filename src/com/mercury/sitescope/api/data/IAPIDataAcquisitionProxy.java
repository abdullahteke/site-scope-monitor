package com.mercury.sitescope.api.data;

public class IAPIDataAcquisitionProxy implements com.mercury.sitescope.api.data.IAPIDataAcquisition {
  private String _endpoint = null;
  private com.mercury.sitescope.api.data.IAPIDataAcquisition iAPIDataAcquisition = null;
  
  public IAPIDataAcquisitionProxy() {
    _initIAPIDataAcquisitionProxy();
  }
  
  public IAPIDataAcquisitionProxy(String endpoint) {
    _endpoint = endpoint;
    _initIAPIDataAcquisitionProxy();
  }
  
  private void _initIAPIDataAcquisitionProxy() {
    try {
      iAPIDataAcquisition = (new com.mercury.sitescope.api.data.SiteScopeExternalDataAPILocator()).getAPIDataAcquisitionImpl();
      if (iAPIDataAcquisition != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iAPIDataAcquisition)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iAPIDataAcquisition)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iAPIDataAcquisition != null)
      ((javax.xml.rpc.Stub)iAPIDataAcquisition)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.mercury.sitescope.api.data.IAPIDataAcquisition getIAPIDataAcquisition() {
    if (iAPIDataAcquisition == null)
      _initIAPIDataAcquisitionProxy();
    return iAPIDataAcquisition;
  }
  
  public byte[] getData(java.lang.String[] in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.mercury.sitescope.api.configuration.exception.ExternalServiceAPIException{
    if (iAPIDataAcquisition == null)
      _initIAPIDataAcquisitionProxy();
    return iAPIDataAcquisition.getData(in0, in1, in2);
  }
  
  public byte[] getDataWithTopology(java.lang.String[] in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.mercury.sitescope.api.configuration.exception.ExternalServiceAPIException{
    if (iAPIDataAcquisition == null)
      _initIAPIDataAcquisitionProxy();
    return iAPIDataAcquisition.getDataWithTopology(in0, in1, in2);
  }
  
  public byte[] getMonitorTypesWithMetricNames(boolean in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException, com.mercury.sitescope.api.configuration.exception.ExternalServiceAPIException{
    if (iAPIDataAcquisition == null)
      _initIAPIDataAcquisitionProxy();
    return iAPIDataAcquisition.getMonitorTypesWithMetricNames(in0, in1, in2);
  }
  
  
}