package com.wscreation.d2rqmappingfile;


public class D2rqParamaters {
	
	public String user="";
	public String password="";
	public String typeDeDriver="";
	public String baseURI="";
	public String jdbcURL=null;
	public String outFile="";
	public String driverClass;
	
	public D2rqParamaters(){}
	
	
	//constructor
	public D2rqParamaters(String user, String password, String typeDeDriver, String baseURI, String jdbcURL, String outfile){
		this.user=user;
		this.password=password;
		this.typeDeDriver=typeDeDriver;
		this.baseURI=baseURI;
		this.jdbcURL=jdbcURL;
		this.outFile=outfile;
		findDriverClass();
	}
	
	/**the typeDeDriver property is used to find the driver class
	 * 
	 * @return String
	 */
	public String findDriverClass() {
		if(typeDeDriver=="mysql"){
			return driverClass="com.mysql.jdbc.Driver";
		}
		else if(typeDeDriver=="postgresql"){
			return driverClass="org.postgresql.Driver";
		}
		else if(typeDeDriver=="oracle"){
			return driverClass="oracle.jdbc.OracleDriver";
		}
		else{
			return driverClass=null;
		}
	}

}
