package com.wscreation.d2rqmappingfile;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * class used to write the mapping file
 * @author WOLLBRETT
 *
 */
public class Writter {
	Model model;
	Map<String, String> prefix;
	List<Resource> tables;
	
	public Writter(Model model,File outputFile) {
		this.model=model;
		String write=write();
		try{
			FileWriter out=new FileWriter(outputFile, false);
			BufferedWriter output = new BufferedWriter(out); 
			output.write(write);
			output.flush();
			output.close(); 
			
		}
		catch(FileNotFoundException e){System.out.println(e.getMessage());}
		catch(IOException e){System.out.println(e.getMessage());}
	}
	
	private String write(){
		String write="";
		write=writePrefix()+"\n"+writeDatabases();
		tables=listTables();
		for(Iterator<Resource> i=tables.iterator();i.hasNext();){
			Resource table=i.next();
			write+=writeResource(table);
			List<Resource> attributs=listTableAttributs(table);
			for(Iterator<Resource> j=attributs.iterator();j.hasNext();){
				Resource attribut=j.next();
//				write+=attribut+"\n";
				write+=writeResource(attribut);
			}
			write+="\n";
		}
		return write;
	}
	
	private String writePrefix(){
		String write="";
		prefix=model.getNsPrefixMap();
		for (Iterator<String> i = prefix.keySet().iterator() ; i.hasNext() ; ){
		    String key=i.next();
		    String value=prefix.get(key);
		    write+="@prefix "+key+": "+"<"+value+"> .\n";
		}
		return write;
	}
	
	private String writeDatabases(){
		String write="";
		List<Resource> databases=listDatabases();
		for(Iterator<Resource> i=databases.iterator();i.hasNext();){
			Resource db=i.next();
			write+="map:"+db.getLocalName()+" a "+"d2rq:Database;";
			StmtIterator stmts=model.listStatements();
			List<String> tri=new ArrayList<String>();
			//sélectionne les prédicats à écrire
			while(stmts.hasNext()){
				Statement stmt=stmts.next();
				if(stmt.getSubject().getLocalName().equals(db.getLocalName())){
					String ns=stmt.getPredicate().getNameSpace();
					if(ns.equals(prefix.get("d2rq"))){
						tri.add("d2rq:"+stmt.getPredicate().getLocalName()+" \""+stmt.getObject().toString()+"\";");
					}
					else if(ns.equals(prefix.get("jdbc"))){
						tri.add("jdbc:"+stmt.getPredicate().getLocalName()+" \""+stmt.getObject()+"\";");
					}
					//A FAIRE: ajouter possibilité d'avoir des prédicats issus d'autres vocabulaires que jdbc et d2rq
				}
			}
			//tri les predicats
			Comparator <String> comp=new Comparator<String>() {
				public int compare(String f1, String f2){
					
					return f1.toString().compareTo(f2.toString());
				}
			};
			Collections.sort(tri,comp);
			for(Iterator<String> j=tri.iterator();j.hasNext();){
				String rs=j.next();
				write+="\n\t"+rs;
			}
			write+="\n\t.\n\n";
			
		}
		return write;
	}
	
	private String writeResource(Resource resource){
		String write="";
		if(isATable(resource)){
			write+="# Table "+resource.getLocalName()+"\n";
		}
		List<Statement> tp=listTableProperties(resource);
		for(Iterator<Statement> j=tp.iterator();j.hasNext();){
			Statement stmt=j.next();
			String predNs=stmt.getPredicate().getNameSpace();
			if(predNs.equals(prefix.get("rdf"))){
				if(stmt.getObject().isResource()&&stmt.getPredicate().getLocalName().equals("type")){
					Resource object=(Resource)stmt.getObject();
					write+="map:"+stmt.getSubject().getLocalName()+" a d2rq:"+object.getLocalName()+";\n";
				}
			}
		}
		for(Iterator<Statement> j=tp.iterator();j.hasNext();){
			Statement stmt=j.next();
			String predNs=stmt.getPredicate().getNameSpace();
			if(predNs.equals(prefix.get("d2rq"))){
				if(stmt.getObject().isResource()){
					Resource object=(Resource)stmt.getObject();
					String objectNs=object.getNameSpace();
					write+="\td2rq:"+stmt.getPredicate().getLocalName()+" ";
					Set <String> keys = prefix.keySet();
					Collection <String> values=prefix.values();
					Iterator<String> it1 = keys.iterator();
					Iterator<String> it2 =values.iterator();
					while (it1.hasNext()) {
						String key = it1.next();
						String value = it2.next();
						if(value.equals(objectNs)){
							write+=key+":"+object.getLocalName()+";\n";
						}
					}
				}
				else{
					write+="\td2rq:"+stmt.getPredicate().getLocalName()+" \""+stmt.getObject()+"\";\n";
				}
			}
			else if(!predNs.equals(prefix.get("d2rq"))){
				
//				if(stmt.getObject().isResource()){
//					Resource object=(Resource)stmt.getObject();
//					String objectNs=object.getNameSpace();
				String predicatNs=stmt.getPredicate().getNameSpace();
				Set <String> keys = prefix.keySet();
				Collection <String> values=prefix.values();
				Iterator<String> predIt1 = keys.iterator();
				Iterator<String> predIt2 =values.iterator();
				while (predIt1.hasNext()) {
					String key = predIt1.next();
					String value = predIt2.next();
					if(value.equals(predicatNs)){
						if(!(key.equals("rdf")&&stmt.getPredicate().getLocalName().equals("type"))){
						write+="\t"+key+":"+stmt.getPredicate().getLocalName()+" ";
						
//						System.out.println(stmt);
						Iterator<String> objIt1 = keys.iterator();
						Iterator<String> objIt2 =values.iterator();
						while (objIt1.hasNext()) {
							String objKey = objIt1.next();
							String objValue = objIt2.next();
							if(stmt.getObject().isResource()){
								Resource object=(Resource)stmt.getObject();
								String objectNs=object.getNameSpace();
								if(objValue.equals(objectNs)){
									write+=objKey+":"+object.getLocalName()+";\n";
								}
							}
							
						}
						if(!stmt.getObject().isResource()){
							write+=stmt.getObject()+";\n";
						}
						}
					}
				}
			}
		}
	write+="\t.\n";
	return write;
	}
	
	private List<Resource> listDatabases(){
		List<Resource> databases=new ArrayList<Resource>();
//		Vector<Resource> tables=new Vector<Resource>();
		StmtIterator stmts=model.listStatements();
		int i=0;
		while(stmts.hasNext()){
			Statement stmt=(Statement)stmts.next();
			String pred=stmt.getPredicate().getLocalName();
			if(pred.equals("jdbcDriver")){
				Resource database=stmt.getSubject();
				databases.add(database);
			}
			i++;
		}
		//tri des tables par ordre alphabetique
		Comparator <Resource> comp=new Comparator<Resource>() {
			public int compare(Resource f1, Resource f2){
				return f1.toString().compareTo(f2.toString());
			}
		};
		Collections.sort(databases,comp);
		return databases;
	}
	
	private List<Resource> listTables(){
//		Collection<Resource> tables;
		List<Resource> tables=new ArrayList<Resource>();
//		Vector<Resource> tables=new Vector<Resource>();
		StmtIterator stmts=model.listStatements();
		while(stmts.hasNext()){
			Statement stmt=(Statement)stmts.next();
			String pred=stmt.getPredicate().getLocalName();
			if(pred.equals("uriPattern")){
				Resource table=stmt.getSubject();
				if(tableHasPK(stmt)){
					tables.add(table);
				}
			}
		}
		//tri des tables par ordre alphabetique
		Comparator <Resource> comp=new Comparator<Resource>() {
			public int compare(Resource f1, Resource f2){
				return f1.toString().compareTo(f2.toString());
			}
		};
		Collections.sort(tables,comp);
		return tables;
	}

	private List<Resource> listTableAttributs(Resource table){
		List<Resource> attributs=new ArrayList<Resource>();
		StmtIterator stmts=model.listStatements();
		while(stmts.hasNext()){
			Statement stmt=(Statement)stmts.next();
			if(stmt.getObject().isResource()){
				Resource isItTheTable=(Resource)stmt.getObject();
				if(isItTheTable.equals(table)){
					if(stmt.getPredicate().getLocalName().equals("belongsToClassMap")){
						if(!stmt.getSubject().toString().equals(table+"__label")){
							attributs.add(stmt.getSubject());
						}
					}
				}
			}
		}
		return attributs;
	}


	private List<Statement> listTableProperties(Resource table){
		List<Statement> list=new ArrayList<Statement>();
		StmtIterator stmts=model.listStatements();
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getSubject().getLocalName().equals(table.getLocalName())){
				list.add(stmt);
			}
		}
		return list;
	}

	private boolean isATable(Resource resource){
		boolean isATable=false;
		for(Iterator<Resource> i=tables.iterator();i.hasNext();){
			Resource table=i.next();
			if(table.equals(resource)){
				isATable=true;
			}
		}
		return isATable;
	}

	private boolean tableHasPK(Statement stmt){
		boolean tableHasPK=false;
		String pk=stmt.getObject().toString();
		if(pk.contains("@")){
			tableHasPK=true;
		}
		return tableHasPK;
	}
}
