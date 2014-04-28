package com.wscreation.d2rqmappingfile;

import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.wscreation.findpath.Parcours_d2rq;

public class AddAssociationRelationships {
//	String mappingPath="";
	Vector<PrimaryKeys> primaryKeys=new Vector<PrimaryKeys>();
	Vector<ForeignKeys> foreignKeys=new Vector<ForeignKeys>();
	ParseD2RQ d2rq;
	Model modelToParse;
	Model model=ModelFactory.createDefaultModel();
	
	//ACCESSOR
	public Model getModel(){return model;}
	
	//CONSTRUCTOR
	public AddAssociationRelationships (Model model) {
		this.model=model;
		d2rq=new ParseD2RQ(model);
		modelToParse=d2rq.getModelToParse();
		run();
	}
	
	/**run the detection of association relationships and add them to the D2RQ mapping file
	 * 
	 */
	private void run(){
		StmtIterator stmts=modelToParse.listStatements();
		String drURI="http://database-relationship.com#";
		//parcours tous les statements du modèle
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			
			if(stmt.getPredicate().getLocalName().equals("dataStorage")){
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
							String fk="";
							if(foreignKeys.elementAt(j).contains(" => ")){
								fk=foreignKeys.elementAt(j).split(" => ")[0];
							}
							else if(foreignKeys.elementAt(j).contains(" = ")){
								fk=foreignKeys.elementAt(j).split(" = ")[0];
							}
							else{
								fk=foreignKeys.elementAt(j).split(" <= ")[0];
							}
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
						model.setNsPrefix("dr", drURI);
						for(int i=0;i<foreignKeys.size();i++){
							String associatedTo="";
							if(foreignKeys.elementAt(i).contains(" => ")){
								associatedTo=(foreignKeys.elementAt(i).split(" => ")[1]);
							}
							else if(foreignKeys.elementAt(i).contains(" = ")){
								associatedTo=(foreignKeys.elementAt(i).split(" = ")[1]);
							}
							else{
								associatedTo=(foreignKeys.elementAt(i).split(" <= ")[1]);
							}
							for (int j=0;j<associatedTo.length();j++){
								if(associatedTo.charAt(j)=='.'){
									String ns=stmt.getSubject().getNameSpace();
									Property predicate=new PropertyImpl(drURI+"AssociatedTo");
									Resource node=new ResourceImpl(ns+associatedTo.substring(0, j));
									Statement association=new StatementImpl(stmt.getSubject(),predicate, (RDFNode)node);
//									System.out.println(association);
									model.add(association);
								}
							}
						}
					}
				}
			}
		}
	}
}
