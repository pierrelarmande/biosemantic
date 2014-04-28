package com.wscreation.d2rqmappingfile;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.Map.Entry;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.wscreation.findpath.Table;


public class ParseD2RQ {
	
	private static Boolean DEBUG=false;
	private String mappingPath;
	private Model modelToParse;
	private Map<String, String> namespaces=new HashMap<String, String>();
	private Map<String, String> foreignKeys=new HashMap<String, String>();
	private Vector<Table> tables=new Vector<Table>();
	private Model model;
	
	//Constructor used to preparcours mapping file
	public ParseD2RQ(Model model){
		this.model=model;
		preparcours();
		listTables();
		listForeignKeys();
	}
	
	public ParseD2RQ(){}
	
	
	public Model getModelToParse(){return modelToParse;}
	public void setModel(Model model){this.model=model;this.modelToParse=model;}
	public Model getModel(){return model;}
	public Map<String, String> getNamespaces(){return namespaces;}
	public Map<String, String>getForeignKeys(){return foreignKeys;}
	public Vector<Table> getTables(){return tables;}
	
	private void readModel(){
		model=ModelFactory.createDefaultModel();
		model.read(new File(mappingPath).toURI().toString(),"N3");
	}
	
	/**premier parcours du graphe avant de chercher un chemin. 
	 * Ce parcours permet de supprimer les predicats non utilisés dans la recherche de chemin.
	 * A la fin de ce parcours il ne reste que des prédicats utiles pour rechercher un chemin dans le graphe
	 * Ces prédicats sont les suivants: 
	 * join: clé étrangère
	 * URIpattern: clé(s) primaire(s) d'une table
	 * dataStorage: BD dans laquelle est contenue la table
	 * refersToClassMap: table dans laquelle est contenue la colonne (clé primaire?)
	 * belongToClassMap: table dans laquelle est contenue la colonne
	**/	
	private void preparcours(){
		modelToParse=ModelFactory.createDefaultModel();
		getNamespaces(model);
		StmtIterator stmts=model.listStatements();
		if(DEBUG==true){
			System.out.println("\nliste des statements après épuration:");
		}
		while(stmts.hasNext()){
			Statement stmt=(Statement)stmts.next();
			String pred=stmt.getPredicate().getLocalName();
			if(pred.equals("join")||pred.equals("uriPattern")||pred.equals("dataStorage")||pred.equals("refersToClassMap")||pred.equals("belongsToClassMap")||pred.equals("class")||pred.equals("subClassOf")||pred.equals("AssociatedTo")||pred.equals("Arity")){
				if(DEBUG==true){
					System.out.println(stmt.getSubject().getLocalName()+" "+stmt.getPredicate()/*.getLocalName()*/+" "+stmt.getObject().toString());
				}
				modelToParse.add(stmt);
			}
			else if(pred.equals("property")){
				if(stmt.getObject().isResource()){
					Resource object=(Resource)stmt.getObject();
					String ns=object.getNameSpace();
					
					String nsRDF=namespaces.get("rdfs");
					String nsVocab=namespaces.get("vocab");
					if(!(ns.equals(nsRDF)||ns.equals(nsVocab))){
						if(DEBUG==true){
							System.out.println(stmt.getSubject().getLocalName()+" "+stmt.getPredicate()/*.getLocalName()*/+" "+stmt.getObject().toString());
						}
						modelToParse.add(stmt);
					}
				}

			}
		}
		associationTables();
		if(DEBUG==true){
			System.out.println("\nnombre de statements dans le modèle de départ: "+model.size());
			System.out.println("\nnombre de statements dans le modèle épuré: "+modelToParse.size());
		}
	}
	
	
	/** récupère tous les namespaces du fichier de mapping**/
	private void getNamespaces(Model m){
		namespaces=m.getNsPrefixMap();
		Iterator<Entry<String, String>> it=namespaces.entrySet().iterator();
		if(DEBUG==true){
			System.out.println("\nliste des namespaces:");
		}
		while (it.hasNext()){
			Map.Entry<String, String> namespace=(Map.Entry<String, String>)it.next();
			if(DEBUG==true){
				System.out.println(namespace.getKey()+" => "+namespace.getValue());
			}
			
		}
	}
	
	/**création d'un vecteur qui regroupe toutes les tables du fichier de mapping*/
	private void listTables(){
		StmtIterator stmts=modelToParse.listStatements();
		if(DEBUG==true){
			System.out.println("\nliste des tables:");
		}
		int i=0;
		while(stmts.hasNext()){
			Statement stmt=(Statement)stmts.next();
			String pred=stmt.getPredicate().getLocalName();
			if(pred.equals("uriPattern")){
				Resource table=stmt.getSubject();
				if(DEBUG==true){
					System.out.println(table);//.getLocalName());
				}
				tables.add(new Table(table));
				i++;
			}
		}
		if(DEBUG==true){
			System.out.println("\nnombre de tables dans le fichier de mapping: "+i);
		}
	}
	
	
	/**liste de toutes les clés étrangères. Cette liste est nécessaire pour trouver les tables ayant une clé étrangère vers la table en cours de traitement.
	 * les statements ayant le predicat join permettent de trouver les clés étrangères dans le sens clé étrangère vers table d'origine (key ver value pour la map).
	 * Cette liste est nécessaire pour parcourir les clés étrangères dans le sens table d'origine vers la clé étrangère (value vers key pour la map)
	 * @return
	 */
	private void listForeignKeys(){
		Property join=modelToParse.getProperty("http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#join");
		NodeIterator nodeIt=modelToParse.listObjectsOfProperty(join);
		if(DEBUG==true){
			System.out.println("\nliste des clés étrangères du fichier de mapping:");
		}
		while (nodeIt.hasNext()){
			String object=nodeIt.next().toString();
			String[] keyAndValue=new String[1];
			if(object.contains(" = ")){
				keyAndValue=object.split(" = ");
			}
			else if(object.contains(" => ")){
				keyAndValue=object.split(" => ");
			}
			else{
				keyAndValue=object.split(" <= ");
			}
			foreignKeys.put(keyAndValue[0], keyAndValue[1]);
		}
	}
	/**modifie les statements associés à un table d'association. Simule la création d'un objet ClassMap pour chaque table d'association. Permet de prendre en compte les tables d'association
	 * 
	 */
	private void associationTables(){
		StmtIterator stmts=modelToParse.listStatements();
		Vector<Resource> list=new Vector<Resource>();
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getPredicate().getLocalName().equals("join")){	
				Resource subject=stmt.getSubject();
				if(stmts.hasNext()){
					Statement stmtnext=stmts.next();
					Resource nextSubject=stmtnext.getSubject();
					if(nextSubject.toString()==subject.toString()&&stmtnext.getPredicate().getLocalName().equals("join")){
						list.add(subject);
					}
				}
			}
		}
//		Property classMap=modelToParse.getProperty("http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#ClassMap");
		for(int i=0;i<list.size();i++){
			Resource database=null;
			Property property=null;
			Resource res=null;
			Vector<String> uriPattern=new Vector<String>();
//			modelToParse.add(list.elementAt(i),classMap, "piou");
			Vector<Statement> associationTables=infosSurUneColonne(list.elementAt(i));
//			System.out.println(associationTables.size());
			for(int j=0;j<associationTables.size();j++){
				Statement stmt=associationTables.elementAt(j);
				if(stmt.getPredicate().getLocalName().equals("belongsToClassMap")){
					Resource table=(Resource)stmt.getObject();
					Vector<Statement> tablestmts=infosSurUneTable(table);
					for(int k=0;k<tablestmts.size();k++){
						if(tablestmts.elementAt(k).getPredicate().getLocalName().equals("dataStorage")){
//							System.out.println("table stmt "+tablestmts.elementAt(k));
							database=(Resource)tablestmts.elementAt(k).getObject();
							property=tablestmts.elementAt(k).getPredicate();
						}
					}
					/*MODIFICATION LE 30/09/11
					 * ajout de la condition if pour corriger un bug*/
					if(database!=null&&property!=null){
//						System.out.println(tablestmts);
//						System.out.println("db "+database);
//						System.out.println("prop "+property);
//						System.out.println("line "+list.elementAt(i));
						modelToParse.remove(stmt);
						modelToParse.add(list.elementAt(i),property,database);
					}
					
				}
				else if(stmt.getPredicate().getLocalName().equals("join")){
					String object=stmt.getObject().toString();
					String[] keyAndValue=new String[1];
					if(object.contains(" => ")){
						keyAndValue=object.split(" => ");
					}
					else if(object.contains(" = ")){
						keyAndValue=object.split(" = ");
					}
					else{
						keyAndValue=object.split(" <= ");
					}
					String key=keyAndValue[0].substring(0,keyAndValue[0].indexOf("."));
					String value="";
					for(int k=0;k<keyAndValue[1].length();k++){
						if(keyAndValue[1].charAt(k)=='.'){
							value=keyAndValue[1].substring(0,k);
						}
					}
//					System.out.println(value);
					uriPattern.add(keyAndValue[0]);
					if(key.equals(list.elementAt(i).getLocalName().toString())){
						res=new ResourceImpl(list.elementAt(i)+"_"+keyAndValue[0].substring(keyAndValue[0].indexOf(".")+1));
						modelToParse.add(res,new PropertyImpl("http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#join"),stmt.getObject());
						String[] refers=list.elementAt(i).toString().split("#");
						
						modelToParse.add(res,new PropertyImpl("http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#refersToClassMap"),new ResourceImpl(refers[0]+"#"+value));
						modelToParse.add(res,new PropertyImpl("http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#belongsToClassMap"),(RDFNode)list.elementAt(i));
						modelToParse.remove(stmt);
					}
				}
				else{
					modelToParse.remove(stmt);
				}
			}
			if(uriPattern.size()>0){
				String pattern=list.elementAt(i).getLocalName();
				for(int j=0;j<uriPattern.size();j++){
					pattern+="/@@"+uriPattern.elementAt(j)+"@@";
				}
				modelToParse.add(list.elementAt(i),new PropertyImpl("http://www.wiwiss.fu-berlin.de/suhl/bizer/D2RQ/0.1#uriPattern"),pattern);
			}
		}
	}
	
	public boolean hasForeignKeys(Vector<Statement> columnStatements){
		int tableNameLength;
		String columnLocation="";
		for(int i=0;i<columnStatements.size();i++){
			Statement stmt=columnStatements.elementAt(i);
			if(stmt.getPredicate().getLocalName().equals("belongsToClassMap")){
				Resource object=(Resource)stmt.getObject();
				tableNameLength=object.getLocalName().length();
				String columnName=stmt.getSubject().getLocalName();
				columnLocation=columnName.substring(0, tableNameLength)+"."+columnName.substring(tableNameLength+1);
			}
		}
		if(foreignKeys.containsKey(columnLocation)){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	public Vector<Statement> infosSurUneTable(Resource table){
		Vector<Statement> infosTable=new Vector<Statement>();
		StmtIterator stmts=modelToParse.listStatements();
		if(DEBUG==true){
			System.out.println("statements associés à la table "+table.getLocalName()+":");
		}
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getSubject().toString()==table.toString()||stmt.getObject().toString()==table.toString()){
				if(DEBUG==true){
					System.out.println(stmt.toString());
				}
				infosTable.add(stmt);
			}
		}
		stmts=modelToParse.listStatements();
		for(int i=0;i<infosTable.size();i++){
			String subject=infosTable.elementAt(i).getSubject().toString();
			String predicate=infosTable.elementAt(i).getPredicate().getLocalName();
			if(predicate.equals("belongsToClassMap")){
				while(stmts.hasNext()){
					Statement stmt=stmts.next();
					if(stmt.getSubject().toString()==subject&&stmt.getPredicate().getLocalName().equals("property")){
						if(DEBUG==true){
							System.out.println(stmt.toString());
						}
						infosTable.add(stmt);
					}
				}
			}	
		}
		return infosTable;
	}
	
	public Resource columnIsInTable(Resource column){
		Resource table=null;
		Vector <Statement> columnInfo=infosSurUneColonne(column);
		for(int i=0;i<columnInfo.size();i++){
			if (columnInfo.elementAt(i).getPredicate().getLocalName().equals("belongsToClassMap")){
				table=(Resource)columnInfo.elementAt(i).getObject();
			}
		}

		return table;
	}
	
	public Resource getTableResource(Vector<Statement> vect){
		Resource table=null;
		for(int i=0;i<vect.size();i++){
			Statement stmt=vect.elementAt(i);
			if(stmt.getPredicate().getLocalName().equals("belongsToClassMap")){
				i=vect.size();
				table=(Resource)stmt.getObject();
			}
		}
		return table;
	}
	
	public Resource inWhichTableIsTheColumn(Resource res){
		Resource table=null;
		Vector<Statement> infosColonne=infosSurUneColonne(res);
		for(int j=0;j<infosColonne.size();j++){
			String predicate=infosColonne.elementAt(j).getPredicate().getLocalName();
			if(predicate.equals("belongsToClassMap")){
				table=(Resource)infosColonne.elementAt(j).getSubject();
			}
		}
		return table;
	}

	//renvoie le nombre de colonnes qui possèdent l'annotation sélectionnée
	public int numberOfColumnWithThisAnnotation(Resource annotation){
		StmtIterator it=modelToParse.listStatements();
		int i=0;
		while(it.hasNext()){
			Statement stmt=it.next();
			if(stmt.getObject().isResource()){
				Resource object=(Resource)stmt.getObject();
				if(object.equals(annotation)){
					i++;
				}
			}
		}
		if(DEBUG==true){
			System.out.println("number of columns annotated with the concept "+annotation.getLocalName()+": "+i);
		}
		return i;
	}

	public Vector<Statement> whereIsTheAnotation(Resource concept){
//		Resource columnName=null;
		Vector<Statement> infosColonne=infosSurUneColonne(concept);
		int annotNumber=numberOfColumnWithThisAnnotation(concept);
		if(annotNumber==0){
			infosColonne.clear();
//			System.out.println("no annotation for the concept: "+concept.toString());
		}
		else if(annotNumber==1){
//			path.add(new PathNode(concept,lengthOfThePath,PathNode.ISTHEINPUTANNOTATION));
			infosColonne=retrieveColumnWithThisAnnotation(concept);
			
		}
		else{
			System.out.println("Le concept est utilisé plusieurs fois pour annoter le même schéma");
			infosColonne=chooseAColumn(infosColonne);
//			for(int j=0;j<infosColonne.size();j++){
//				String predicate=infosColonne.elementAt(j).getPredicate().getLocalName();
//				String object=infosColonne.elementAt(j).getObject().toString();
////			if(infos)
//			}
		}
		return infosColonne;
	}
	
	//renvoie les statement de la colonne annotée avec le concept sélectionné
	private Vector<Statement> retrieveColumnWithThisAnnotation(Resource annotation){
		Vector<Statement> stmts=new Vector<Statement>();
		StmtIterator it=modelToParse.listStatements();
		while(it.hasNext()){
			Statement stmt=it.next();
			if(stmt.getObject().isResource()){
				Resource object=(Resource)stmt.getObject();
				if(object.equals(annotation)){
					stmts=infosSurUneColonne(stmt.getSubject());
					if(DEBUG==true){
						System.out.println("the concept "+annotation.getLocalName()+" annotate the column "+stmt.getSubject());
					}
				}
			}
		}
		return stmts;
	}

	//si plusieurs colonnes d'une base de données sont annotées avec le même concept il est possible de choisir la colonne souhaitée
	//A MODIFIER=> il faut traiter toutes les colonnes annotées avec un même concept ontologique
	public Vector<Statement> chooseAColumn(Vector<Statement> column){
		System.out.println("quelle colonne?");
		for(int i=0;i<column.size();i++){
			System.out.println((i+1)+". "+column.elementAt(i).getSubject());
		}
		Scanner sc=new Scanner(System.in);
		int str=sc.nextInt();
		return infosSurUneColonne(column.elementAt(str-1).getSubject());
	}
	
	/**permet de recupérer toutes les iformtations sur une colonne. 
	 * Ces informations sont l'annotation associée, la table dans laquelle elle est contenue et les clés étrangères dont elle dépend
	 * 
	 * @param colonne
	 * @return Vector<Statement>
	 */
	public Vector<Statement> infosSurUneColonne(Resource colonne){
		Vector<Statement> infosColonne=new Vector<Statement>();
		StmtIterator stmts=modelToParse.listStatements();
		if (DEBUG==true){
			System.out.println("\nstatements associés à la colonne "+colonne.getLocalName()+":");
		}
		while(stmts.hasNext()){
			
			Statement stmt=stmts.next();
			if(stmt.getSubject().equals(colonne)||stmt.getObject().toString()==colonne.toString()){
				if(DEBUG==true){
					System.out.println(stmt.toString());
				}
				infosColonne.add(stmt);
			}
		}
		return infosColonne;
	}

	public Resource refersToDatabase(Resource table) {
		Vector<Statement> tableInfos=infosSurUneTable(table);
		Resource database= null;
		for(int i=0;i<tableInfos.size();i++){
			if(tableInfos.elementAt(i).getPredicate().getLocalName().equals("dataStorage")){
				database=(Resource)tableInfos.elementAt(i).getObject();
			}
		}
		return database;
	}
	
	public int getArity(Resource table){
		StmtIterator stmts=model.listStatements();
		int arity=-1;
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getSubject().equals(table)){
				if(stmt.getPredicate().getLocalName().equals("Arity")){
					arity=Integer.parseInt(stmt.getObject().asNode().getLiteralLexicalForm());
				}
			}
		}
		
//		Vector<Statement> tableInfos=infosSurUneTable(table);
//		String relation="";
//		System.out.println(table.getLocalName());
//		for(int i=0;i<tableInfos.size();i++){
//			Statement tableInfo=tableInfos.elementAt(i);
//			if(tableInfo.getPredicate().getLocalName().equals("subClassOf")){
//				relation="subClassOf";
//				i=tableInfos.size();
//			}
//			else if(tableInfo.getPredicate().getLocalName().equals("AssociatedTo")){
//				relation="AssociatedTo";
//				i=tableInfos.size();
//			}
//		}
//		for(int i=0;i<tableInfos.size();i++){
//			Statement tableInfo=tableInfos.elementAt(i);
//			if(tableInfo.getPredicate().getLocalName().equals("Arity")){
//				arity=Integer.parseInt(tableInfo.getObject().asNode().getLiteralLexicalForm());
//				System.out.println(table.getLocalName()+" "+arity);
//			}
//		}
		return arity;
	}
	

	//test de suppression de predicats.
//	public static void main(String[] args) {
//		Parcours_d2rq d2rq=new Parcours_d2rq("mapping-TG_LOCAL_RICE_modifie.n3");
//		Model model=d2rq.modelToParse;
//		OutputStream out=null;
//		File outFile=new File("c:\\RDF_epure.rdf");
//		try{
//			outFile.createNewFile();
//			out=new FileOutputStream(outFile);
//		}
//		catch(FileNotFoundException e){System.out.println(e.getMessage());}
//		catch(IOException e){System.out.println(e.getMessage());}
//		model.write(out, "RDF/XML");
//		File file=new File("mapping-TG_LOCAL_RICE.n3");
//		Model model=ModelFactory.createDefaultModel();
//		Model modelLight=ModelFactory.createDefaultModel();
//		model.read(file.toURI().toString(),"N3");
//		namespaces=model.getNsPrefixMap();
//		Iterator it=namespaces.entrySet().iterator();
//		while (it.hasNext()){
//			Map.Entry namespace=(Map.Entry)it.next();
//			System.out.println(namespace.getKey()+" => "+namespace.getValue());
//		}
//		Resource inputResource=model.getResource("http://gcpdomainmodel.org/GCPDM#GCP_SimpleIdentifier");
//		System.out.println(model.getReader());
		
//		StmtIterator stmts=model.listStatements();
//		int i=0;
//		while(stmts.hasNext()){
//			Statement stmt=(Statement)stmts.next();
//			String pred=stmt.getPredicate().getLocalName();
//			String subject=stmt.getSubject().getLocalName();
//			String object=stmt.getObject().toString();
//			if(pred.equals("join")||pred.equals("uriPattern")||pred.equals("dataStorage")||pred.equals("refersToClassMap")||pred.equals("belongsToClassMap")){
////				if(subject==object){
//				modelLight.add(stmt);
//				System.out.println(stmt.getSubject().getLocalName()+" "+stmt.getPredicate().getLocalName()+" "+stmt.getObject().toString());
////				}
//				i++;	
//			}
//			
//		}
//		System.out.println(i);
//		System.out.println(inputResource);
//		Graph graphe=model.getGraph();
//		System.out.println(graphe.toString());
//	}

}
