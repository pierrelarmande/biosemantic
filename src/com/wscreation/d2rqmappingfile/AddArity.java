package com.wscreation.d2rqmappingfile;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.wscreation.findpath.Parcours_d2rq;

public class AddArity {
	
	Model model;
	ParseD2RQ d2rq;
	Model modelToParse;
	
	//ACCESSOR
	public Model getModel(){return model;}
	
	public AddArity (Model model) {
		this.model=model;
		d2rq=new ParseD2RQ(model);
		modelToParse=d2rq.getModelToParse();
		run();
	}
	
	/**run the arity detection and add these arity to the D2RQ mapping file
	 * 
	 */
	private void run(){
		StmtIterator stmts=modelToParse.listStatements();
		Map<Resource, Integer> tableArity=new HashMap<Resource, Integer>();
		//parcours tous les statements du modèle
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getPredicate().getLocalName().equals("join")){
				Vector<Statement> columns=d2rq.infosSurUneColonne(stmt.getSubject());
				for (int i=0;i<columns.size();i++){
					
					Statement column=columns.elementAt(i);
					if(column.getPredicate().getLocalName().equals("belongsToClassMap")){
						if(tableArity.containsKey((Resource)column.getObject())){
							
							int arity=tableArity.get((Resource)column.getObject());
							tableArity.put((Resource)column.getObject(), arity+1);
						}
						else{
							tableArity.put((Resource)column.getObject(), 1);
						}
					}
//					else if(column.getPredicate().getLocalName().equals("refersToClassMap")){
//						System.out.println(column.getObject());
//						if(tableArity.containsKey((Resource)column.getObject())){
//							int arity=tableArity.get((Resource)column.getObject());
////							if(arity>=1){
//								tableArity.put((Resource)column.getObject(), arity+1);
////							}
//						}
//						else{
//							tableArity.put((Resource)column.getObject(), 1);
//						}
//					}
				}
			}
		}
		for(Entry<Resource, Integer> entry : tableArity.entrySet()) {
		    Integer valeur = entry.getValue();
		    if(valeur>1){
		    	Resource cle = entry.getKey();
			    String drURI="http://database-relationship.com#";
			    Property predicate=new PropertyImpl(drURI+"Arity");
			    RDFNode node = model.createTypedLiteral("\""+valeur.toString()+"\"","rdf:int");
			    Statement arityStmt=new StatementImpl(cle, predicate,node);
			    model.add(arityStmt);
		    }
		}
	}
}
