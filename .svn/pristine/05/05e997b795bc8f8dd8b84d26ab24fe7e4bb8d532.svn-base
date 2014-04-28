package com.wscreation.test;

import java.io.File;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.wscreation.findpath.Parcours_d2rq;

public class FindAssociationTest {
	public static void main(String[] args) {
		int number=0;
//		String mappingPath="mapping-TG_LOCAL_RICE.n3";
		String mappingPath="mapping_test.n3";
		Model model=ModelFactory.createDefaultModel();
//		ModelD2RQ model=new ModelD2RQ(mappingPath);
		model.read(new File(mappingPath).toURI().toString(),"N3");
		Parcours_d2rq d2rq=new Parcours_d2rq(model);
		Model modelToParse=d2rq.getModelToParse();
		StmtIterator stmts=modelToParse.listStatements();
		//parcours tous les statements du modèle
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getPredicate().getLocalName().equals("class")){
				Vector<String> foreignKeys=new Vector<String>();
				Vector<String> primaryKeys=new Vector<String>();
				Resource table=stmt.getSubject();
				Vector<Statement> tablestmts=d2rq.infosSurUneTable(table);
				for(int i=0;i<tablestmts.size();i++){
					Statement currentStmt=tablestmts.elementAt(i);
					if(currentStmt.getPredicate().getLocalName().equals("uriPattern")){
						String uriPattern=tablestmts.elementAt(i).getObject().toString();
						String [] pk=uriPattern.split("@@");
						for(int j=1;j<pk.length;j++){
							if(j%2!=0){
								primaryKeys.add(pk[j]);
							}
						}
					}
					if(currentStmt.getPredicate().getLocalName().equals("belongsToClassMap")){
						Resource column=currentStmt.getSubject();
						Vector<Statement> columnstmts=d2rq.infosSurUneTable(column);
						for(int j=0;j<columnstmts.size();j++){
							Statement currentStmtColumn=columnstmts.elementAt(j);
							if(currentStmtColumn.getPredicate().getLocalName().equals("join")){
								foreignKeys.add(currentStmtColumn.getObject().toString());
							}
						}
					}
				}
				if(primaryKeys.size()==foreignKeys.size()){
					boolean find=false;
					for(int i=0;i<primaryKeys.size();i++){
						String pk=primaryKeys.elementAt(i);
						for(int j=0;j<foreignKeys.size();j++){
							String fk=foreignKeys.elementAt(j).split(" = ")[0];
							if(fk.equals(pk)){
								find=true;
								j=foreignKeys.size();
							}
							if(j==foreignKeys.size()){
								if(find==false){
									i=primaryKeys.size();
								}
							}
						}
					}
					if(find==true){
//						System.out.println("#table "+stmt.getSubject());
						for(int i=0;i<foreignKeys.size();i++){
//							System.out.println(foreignKeys.elementAt(i).split(" = ")[1]);
							String associatedTo=(foreignKeys.elementAt(i).split(" = ")[1]);
							for (int j=0;j<associatedTo.length();j++){
								if(associatedTo.charAt(j)=='.'){
									associatedTo=associatedTo.substring(0, j);
								}
							}
//							associatedTo=associatedTo.split(".")[1];
							number++;
							System.out.println(stmt.getSubject().getLocalName()+"   ns:associatedTo "+stmt.getSubject().getNameSpace()+associatedTo);
						}
					}
				}
				
			}
//			else if (stmt.getPredicate().getLocalName().equals("uriPattern")){
//				System.out.println(stmt.getSubject().getLocalName());
//			}
		}
		System.out.println(number);
	}
}
