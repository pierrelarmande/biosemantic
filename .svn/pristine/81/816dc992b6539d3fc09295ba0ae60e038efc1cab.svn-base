package com.wscreation.d2rqmappingfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;


public class AddHeritageRelationships {
	
	String mappingPath="";
	Vector<PrimaryKeys> primaryKeys=new Vector<PrimaryKeys>();
	Vector<ForeignKeys> foreignKeys=new Vector<ForeignKeys>();
	ParseD2RQ d2rq;
	Model model=ModelFactory.createDefaultModel();
	
	//ACCESSOR
	public Model getModel(){return model;}
	
	//CONSTRUCTOR
	public AddHeritageRelationships(Model model) {
		this.model=model;
//		System.out.println(model.size());
		d2rq=new ParseD2RQ(model);
		run();	
	}
	
	/**run the detection of inheritance relationships and add them to the D2RQ mapping file
	 * 
	 */
	private void run(){
		Model modelToParse=d2rq.getModel();
		StmtIterator stmts=modelToParse.listStatements();
		//parcours tous les statements du modèle
		while(stmts.hasNext()){
			Statement stmt=stmts.next();
			if(stmt.getPredicate().getLocalName().equals("join")){
//				System.out.println("fk");
				findForeignKeys(stmt);
			}
			else if(stmt.getPredicate().getLocalName().equals("dataStorage")){
//				System.out.println("pk");
				findPrimaryKeys(stmt);
			}		
		}
//		System.out.println(primaryKeys.size());
//		System.out.println(foreignKeys.size());
		findHeritageRelationships();
		writeRelationships();
	}
	
	public void findForeignKeys(Statement stmt){
		Resource column=stmt.getSubject();
		Vector<Statement> columnInfos=d2rq.infosSurUneColonne(column);
		Resource table=model.getResource("");
		Resource database=model.getResource("");
		String join="";
		/*MODIFICATIONS LE 30/09/11
		 * en raison d'un problème de détection de clés étrangères
		 * les tables d'associations sans attributs etant des proprietes ne sont pas 
		 * rattachées à une table. Le variable database pouvait donc être null lors de tests
		 * plus d'erreur mais non détection des relations d'héritage
		 * ajout de variable find1 et test que current DB n'est pas null
		 */
		boolean find1=false;
		//parcours toutes les infos sur une colonne contenant une clé étrangère
		for (int i=0;i<columnInfos.size();i++){
			if(columnInfos.elementAt(i).getPredicate().getLocalName().equals("belongsToClassMap")){
//				System.out.println("columninfos "+columnInfos.elementAt(i));
//				System.out.println("pred "+columnInfos.elementAt(i).getPredicate().getLocalName());
				table=(Resource)columnInfos.elementAt(i).getObject();
//				System.out.println("table "+table);
				database=d2rq.refersToDatabase((Resource)columnInfos.elementAt(i).getObject());
//				System.out.println("database "+database);
				find1=true;
			}
			else if(columnInfos.elementAt(i).getPredicate().getLocalName().equals("join")){
				join=columnInfos.elementAt(i).getObject().toString();
//				System.out.println(join);
			}
			
		}
		//booléen permettant de savoir si une table déjà enregistrée est identique à celle trouvée
		
		if(find1==true){
		boolean find=false;
		for (int i=0;i<foreignKeys.size();i++){
			Resource currentTable=foreignKeys.elementAt(i).getTable();
			Resource currentDB=foreignKeys.elementAt(i).getDatabase();
//			System.out.println("cDB "+currentDB);
//			System.out.println("cT "+currentTable);
			if(currentDB==null){
				if(currentTable.equals(table)){
					foreignKeys.elementAt(i).setOneJoin(join);
					find=true;
				}
				ForeignKeys fk=foreignKeys.elementAt(i);
//				System.out.println("T "+fk.getTable());
//				System.out.println("DB "+fk.getDatabase());
//				System.out.println("join "+fk.getJoin());
			}
			else if(currentDB.equals(database)&&currentTable.equals(table)){
//				System.out.println(foreignKeys.size());
				foreignKeys.elementAt(i).setOneJoin(join);
				find=true;
			}
		}
		if(find==false){
			foreignKeys.add(new ForeignKeys(join, table, database));
//			System.out.println("db "+database);
//			System.out.println("table "+table);
//			System.out.println("join "+join);
		}}
//		System.out.println(foreignKeys.size());
	}
	
	private void findPrimaryKeys(Statement stmt){
		Resource table=stmt.getSubject();
		Resource database=(Resource)stmt.getObject();
		String primaryKey="";
		Vector<Statement> tableInfos =d2rq.infosSurUneTable(table);
		//parcours tous les statements d'une table
		for (int i=0;i<tableInfos.size();i++){
			//récupère la clé primaire de la table
			if(tableInfos.elementAt(i).getPredicate().getLocalName().equals("uriPattern")){
				primaryKey=tableInfos.elementAt(i).getObject().toString();
//				System.out.println(primaryKey);
			}
		}
		primaryKeys.add(new PrimaryKeys(primaryKey, table, database));
//		System.out.println(primaryKeys.size());
	}
	
	private void findHeritageRelationships(){
		int number=0;
		for(int i=0;i<primaryKeys.size();i++){
			boolean equal=false;
//			Resource r=primaryKeys.elementAt(i).getTable();
			Resource database=primaryKeys.elementAt(i).getDatabase();
			Vector<String> vectorPK=primaryKeys.elementAt(i).getUriPattern();
			for(int j=0;j<foreignKeys.size();j++){
				//vérifie que les 2 tables sont dans la même base de données
//				System.out.println("fkDB "+foreignKeys.elementAt(j).getDatabase());
//				System.out.println("db "+database);
				Resource fkDB=foreignKeys.elementAt(j).getDatabase();
				if(fkDB!=null&&fkDB.equals(database)){
//					System.out.println("et ca passe");
//					Resource s=foreignKeys.elementAt(j).getTable();
					Vector<String> vectorFK=foreignKeys.elementAt(j).getJoin();
//					System.out.println(vectorFK.size());
					if(vectorFK.size()==vectorPK.size()){
//						System.out.println("blop");
						for(int k=0;k<vectorPK.size();k++){
							equal=false;
							
							for(int l=0;l<vectorFK.size();l++){
								if(vectorFK.elementAt(l).length()>4){
//								System.out.println(vectorFK.elementAt(l));
									String fk="";
									if(vectorFK.elementAt(l).contains(" => ")){
										fk=vectorFK.elementAt(l).split(" => ")[1];
									}
									else if(vectorFK.elementAt(l).contains(" = ")){
										fk=vectorFK.elementAt(l).split(" = ")[1];
									}
									else{
										fk=vectorFK.elementAt(l).split(" <= ")[1];
									}
									if(fk.equals(vectorPK.elementAt(k))){
									equal=true;
									l=vectorFK.size();
								}
								}
//								System.out.println(vectorPK.elementAt(k)+" -> "+vectorFK.elementAt(l));
							}
							if(equal==false){
								k=vectorPK.size();
							}
						}
						if(equal==true){
//							System.out.println("table parent: "+primaryKeys.elementAt(i).getTable().getLocalName()+"\t table héritée: "+foreignKeys.elementAt(j).getTable().getLocalName());
							Property predicate=new PropertyImpl("http://www.w3.org/2000/01/rdf-schema#subClassOf");
							Statement heritage=new StatementImpl(foreignKeys.elementAt(j).getTable(),predicate, (RDFNode)primaryKeys.elementAt(i).getTable());
							model.add(heritage);
							number++;
						}
					}
				}
			}
		}
//		System.out.println("numb "+number);
	}
	
	private void writeRelationships(){
		File outFile=new File("c:\\mapping_heritage.n3");
		OutputStream out=null;
		try{
			outFile.createNewFile();
			out=new FileOutputStream(outFile);
		}
		catch(FileNotFoundException e){System.out.println(e.getMessage());}
		catch(IOException e){System.out.println(e.getMessage());}
		model.write(out,"N3");
	}
}
