package com.wscreation.wsskeleton;

public class MetaInfCreation {
	
	String[] input;
	String[] output;
	public MetaInfCreation(){}
	
	public void createServiceXml(){
		String serviceXml="<service name=\""+WebServicesParameters.WSName+"\" >"
		+"<Description>"
			+wsdescription(input, output)+
		"</Description>"+
		"<messageReceivers>"+
			"<messageReceiver mep=\"http://www.w3.org/2004/08/wsdl/in-only\" class=\"org.apache.axis2.rpc.receivers.RPCInOnlyMessageReceiver\" />"+
			"<messageReceiver  mep=\"http://www.w3.org/2004/08/wsdl/in-out\"  class=\"org.apache.axis2.rpc.receivers.RPCMessageReceiver\"/>"+
		"</messageReceivers>"+
		"<parameter name=\"ServiceClass\" locked=\"false\">"+WebServicesParameters.packageName+"."+WebServicesParameters.WSName+"</parameter>"+
		"<parameter name=\"useOriginalwsdl\">true</parameter>"+
		"<parameter name=\"modifyUserWSDLPortAddress\">true</parameter> "+
    "</service>";
	}
	
	private String wsdescription(String[] input,String[] output){
		String desc="";
		if (input.length!=0){
			desc="This webservice takes as input the concept(s)";
			for(int i=0;i<input.length;i++){
				desc+=" "+input[i];
				if(i+1<input.length){
					desc+=",";
				}
			}
			desc+=" and";
		}
		if (output.length!=0){
			desc="returns the concept(s)";
			for(int i=0;i<output.length;i++){
				desc+=" "+output[i];
				if(i+1<output.length){
					desc+=",";
				}
			}
			desc+=".";
		}
		return desc;
	}
	
	private void createWSDL(){
		
	}

}


