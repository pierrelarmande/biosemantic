package com.wscreation.createrequest;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.wscreation.d2rqmappingfile.ParseD2RQ;
import com.wscreation.findpath.AssociationWithoutAttributs;
import com.wscreation.findpath.FindOnePath;
import com.wscreation.findpath.Parcours_d2rq;
import com.wscreation.findpath.PathNode;

/**
 * Class used to create a SPARQL query
 * @author WOLLBRETT
 *
 */

public class CreateRequest {
	
	//ATTRIBUT
	Vector<PathNode>path=new Vector<PathNode>();
	String mappingPath;
	Resource input;
	Resource output;
	Resource outputTable;
	FindOnePath findOnePath;
	Vector<Resource> inheritanceRelationships=new Vector<Resource>();
	Vector<PathNode> shortestPathNodes=new Vector<PathNode>();
	Vector<String> request=new Vector<String>();
	Vector<AssociationWithoutAttributs> awa=new Vector<AssociationWithoutAttributs>();
	Resource inputAnnot;
	Resource outputAnnot;
	boolean union=false;
	int OUTPUTISSIMPLEPK=1;
	int OUTPUTISMULTIPLEPK=2;
	int outputType=0;
	int inputType=0;
	Resource lastPKWithoutAtt=null;
	Resource lastRelation=null;
	Resource inputTable=null;
	private boolean DEBUG=false;
	
	//CONSTRUCTOR
	public CreateRequest(String mappingPath,Vector<PathNode> path,Resource input, Resource output,Resource inputAnnot,Resource outputAnnot,FindOnePath findOnePath) {
		this.mappingPath=mappingPath;
		this.path=path;
		this.input=input;
		this.inputAnnot=inputAnnot;
		this.output=output;		
		this.outputAnnot=outputAnnot;
		this.findOnePath=findOnePath;
		awa=findOnePath.parcours.getAssociationWithoutAttributs();
		getOutputTable();
		findShortestPath();
		if(DEBUG==true){
			for(int i=0;i<shortestPathNodes.size();i++){
				System.out.println(shortestPathNodes.elementAt(i));
			}
			System.out.println("finish findShortestPath");
		}
		request=buildRequest();
	}
	
	//ACCESSOR
	public Vector<PathNode> getShortestPathNodes(){return shortestPathNodes;}
	public Vector<String> getRequestPath(){return request;}
	
	/**find the shortest path. recursive method calling findshortestPath(Resource, Vector<Resource>, Resource) for each detected inheritance relationship
	 * @return void .directly instanciate the class attribut named shortestPathNodes
	 */
	public void findShortestPath(){
		Vector<Resource> alreadyDetectedHeritage=new Vector<Resource>();
		inputTable=getInputTable(input);
		Resource currentTable=outputTable;
		if(DEBUG==true){
			System.out.println("input table "+inputTable);
			System.out.println("current table "+currentTable);
			System.out.println("output table "+outputTable);
			System.out.println("output annotation "+outputAnnot);
			System.out.println("output resource "+output);
		}
		Vector<PathNode>heritageRelationships=new Vector<PathNode>();
		//retrieve the shortest path for a given input and output
		while(!currentTable.equals(inputTable)){
			double weight=-1;
			PathNode pathnode=null;
			int iter=-1;
			for(int i=0;i<path.size();i++){
				PathNode currentPathNode=path.elementAt(i);
				if(currentPathNode.getObjectNode().equals(currentTable)){
					if(!(currentPathNode.getSubjectNode()==null)){
						if(weight==-1){
							weight=currentPathNode.getWeight();
							pathnode=currentPathNode;
							iter=i;
						}
						else if(currentPathNode.getWeight()<weight){
							weight=currentPathNode.getWeight();
							pathnode=currentPathNode;
							iter=i;
						}
					}
				}	
			}
			
			if(!(pathnode==null)){
				currentTable=pathnode.getSubjectNode();
				shortestPathNodes.add(pathnode);
				boolean findCurrentTableRelation=true;
				boolean findCurrentTableInformation=true;
				for(int i=iter-1;i>=0;i--){
					if(!(path.elementAt(i).getSubjectNode()==null)&&findCurrentTableRelation==true){
//						System.out.println(path.elementAt(i)+" --> "+currentTable);
						if(path.elementAt(i).getSubjectNode().equals(currentTable)){
							if(path.elementAt(i).getRelationType()==-1){
								shortestPathNodes.add(path.elementAt(i));
							}
							else{findCurrentTableRelation=false;}
						}
						else{i=-1;}
					}
					else if(path.elementAt(i).getSubjectNode()==null&&findCurrentTableInformation==true){
						if(path.elementAt(i).getNodeType()==1){
							if(!(path.elementAt(i).getObjectNode().getLocalName().equals(currentTable.getLocalName()))){
								findCurrentTableInformation=false;
							}
						}
						else{
							shortestPathNodes.add(path.elementAt(i));
						}
					}
				}
			}
			else{
				currentTable=inputTable;
			}
		}
		if(DEBUG==true){
			System.out.println("recherche héritage");
		}
		//search inheritance relationships and recursively retrieve corresponding shortest path
		heritageRelationships=detectHeritageRelationships(shortestPathNodes);
		if(DEBUG==true){
			System.out.println("number of inheritance: "+heritageRelationships.size());
		}
		if(heritageRelationships.size()==1){
			if(heritageRelationships.elementAt(0).getObjectNode().equals(outputTable)){
				heritageRelationships.clear();
			}
			else if(heritageRelationships.elementAt(0).getObjectNode().equals(inputTable)){
				heritageRelationships.clear();
			}
		}
		if(heritageRelationships.size()!=0){
			Resource heritageParentInput=heritageRelationships.elementAt(0).getSubjectNode();
			Parcours_d2rq parcours=new Parcours_d2rq(mappingPath,false);
			Vector<Resource> heritageInputs=parcours.findChildren(heritageParentInput);
			if(heritageInputs.size()>1){
				shortestPathNodes.add(null);

				for(int i=0;i<heritageInputs.size();i++){
					if(!(heritageRelationships.size()==0)){
						if(!(alreadyDetectedHeritage.contains(heritageInputs.elementAt(i)))){
							if(DEBUG==true){
								System.out.println("parent table: "+heritageParentInput);
								System.out.println(heritageInputs.elementAt(i));
							}
							shortestPathNodes.addAll(findShortestPath(heritageInputs.elementAt(i),alreadyDetectedHeritage,heritageParentInput));
							alreadyDetectedHeritage.add(heritageInputs.elementAt(i));
						}
						
					}
				}
			}
			heritageRelationships.remove(0);
		}
	}
	/**retrieve the shortest path corresponding to each detecting inheritance relationship
	 * 
	 * @param input input Resource
	 * @param alreadyDetectedHeritage
	 * @param parent; parent of the detected inheritance relationship
	 * @return Vector<PathNode>; a vector containing already detected nodes of the shortest path
	 */
	public Vector<PathNode> findShortestPath(Resource input,Vector<Resource> alreadyDetectedHeritage,Resource parent){
		Vector<PathNode> shortestPathNodes=new Vector<PathNode>();
		Resource inputTable=getInputTable(input);
		findOnePath.setInputColumn(inputTable);
		Resource currentTable=outputTable;
		FindOnePath path2=new FindOnePath(mappingPath, input, output);
		Vector<PathNode> path=path2.findOnePath(true,parent);
		Vector<PathNode>heritageRelationships=new Vector<PathNode>();
		//retrieve the shortest path for a given input and output
		if(DEBUG==true){
			System.out.println("mapping file: "+mappingPath);
			System.out.println("input: "+input);
			System.out.println("output: "+output);
		}
		while(!currentTable.equals(inputTable)){
			int iter=-1;
			double weight=-1;
			PathNode pathnode=null;
			//condition rajoutée car il est possible de ne pas trouver de chemin entre 2 noeuds. Dans ce cas
			//la variable path est null.
			if(path!=null){
			for(int i=0;i<path.size();i++){
				PathNode currentPathNode=path.elementAt(i);
				if(!(currentPathNode.getObjectNode()==null)){
				if(currentPathNode.getObjectNode().equals(currentTable)){
					if(!(currentPathNode.getSubjectNode()==null)){
						if(weight==-1){
							weight=currentPathNode.getWeight();
							pathnode=currentPathNode;
							iter=i;
						}
						else if(currentPathNode.getWeight()<weight){
							weight=currentPathNode.getWeight();
							pathnode=currentPathNode;
							iter=i;
						}
					}
				}
				}
			}
			}
			
			if(!(pathnode==null)){
				currentTable=pathnode.getSubjectNode();
				shortestPathNodes.add(pathnode);
				boolean findCurrentTableRelation=true;
				boolean findCurrentTableInformation=true;
				for(int i=iter-1;i>=0;i--){
					if(!(path.elementAt(i).getSubjectNode()==null)&&findCurrentTableRelation==true){
						if(path.elementAt(i).getSubjectNode().equals(currentTable)){
							if(path.elementAt(i).getRelationType()==-1){
								shortestPathNodes.add(path.elementAt(i));
							}
							else if(path.elementAt(i).getNodeType()==0){
								shortestPathNodes.add(path.elementAt(i));
								findCurrentTableRelation=false;
							}
							else{findCurrentTableRelation=false;}
						}
						else{i=-1;}
					}
					else if(path.elementAt(i).getSubjectNode()==null&&findCurrentTableInformation==true){
						if(path.elementAt(i).getNodeType()==1){
							if(!(path.elementAt(i).getObjectNode().getLocalName().equals(currentTable.getLocalName()))){
								findCurrentTableInformation=false;
							}
						}
						else if(!(path.elementAt(i).getNodeType()==0)){
							shortestPathNodes.add(path.elementAt(i));
						}
					}
						
				}
			}
			else{
				currentTable=inputTable;
			}
		}
		//recherche les relations d'héritage et récupère récursivement les plus courts chemins
		heritageRelationships=detectHeritageRelationships(shortestPathNodes);
		while(heritageRelationships.size()!=0){
			Resource heritageParentInput=heritageRelationships.elementAt(0).getSubjectNode();
			Parcours_d2rq parcours=new Parcours_d2rq(mappingPath,false);
			Vector<Resource> heritageInputs=parcours.findChildren(heritageParentInput);
			if(heritageInputs.size()>1){
				shortestPathNodes.add(null);
				for(int i=0;i<heritageInputs.size();i++){
					if(!(heritageRelationships.size()==0)){
						if(!(alreadyDetectedHeritage.contains(heritageInputs.elementAt(i)))){
//							//recursivity
							Vector<PathNode> temp=findShortestPath(heritageInputs.elementAt(i),alreadyDetectedHeritage,heritageParentInput);
							shortestPathNodes.addAll(temp);
							alreadyDetectedHeritage.add(heritageInputs.elementAt(i));
						}
						else{
							heritageRelationships.remove(0);
						}
					}
				}
			}
		}
		return shortestPathNodes;
	}

	/**retrieve information about the first relation
	 * 
	 * @return int corresponding to the type of the first relation.
	 */
	private int getFirstRelation(){
		boolean findInput=false;
		int firstrelation=-1;
		for(int i=0;i<shortestPathNodes.size();i++){
			PathNode node=shortestPathNodes.elementAt(i);
			if(findInput==false){
				if(!(node==null)){
					if(node.getObjectNode()==input){
						findInput=true;
					}
				}
			}
			else if(!(node.getSubjectNode()==null)){
				if(node.getNodeType()==PathNode.ISANINVERSEFOREIGNKEYCOLUMN){
					firstrelation=PathNode.ISANINVERSEFOREIGNKEYCOLUMN;
					i=shortestPathNodes.size();
				}
				else if(node.getNodeType()==PathNode.ISAFOREIGNKEYCOLUMN){
					firstrelation=PathNode.ISAFOREIGNKEYCOLUMN;
					i=shortestPathNodes.size();
				}
			}
		}
		return firstrelation;
	}
	
	/**retrieve information about the relation relation
	 * 
	 * @return int corresponding to the type of the last relation.
	 */
	private int getLastRelation(){
		boolean findOutput=false;
		int lastrelationtype=-1;
		for(int i=shortestPathNodes.size()-1;i>=0;i--){
			PathNode node=shortestPathNodes.elementAt(i);
			if(findOutput==false){
				if(!(node==null)){
					
					if(node.getObjectNode().equals(outputTable)){
						findOutput=true;
					}
				}
			}
			else if(!(node.getSubjectNode()==null)){
				if(node.getNodeType()==PathNode.ISANINVERSEFOREIGNKEYCOLUMN){
					lastrelationtype=PathNode.ISANINVERSEFOREIGNKEYCOLUMN;
					lastRelation=node.getObjectNode();
					i=0;
				}
				else if(node.getNodeType()==PathNode.ISAFOREIGNKEYCOLUMN){
					lastrelationtype=PathNode.ISAFOREIGNKEYCOLUMN;
					lastRelation=node.getObjectNode();
					i=0;
				}
			}
		}
		return lastrelationtype;
		
	}
	
	/**detect if the first relation contains primary key composed to multiple attributs
	 * true => multiple
	 * false => unique
	 * @return boolean
	 */
	private boolean firstRelationHasMultiplePrimaryKey(){
		boolean findInput=false;
		boolean findNextTable=false;
		boolean firstRelationHasMultiplePrimaryKey=false;
		for(int i=0;i<shortestPathNodes.size();i++){
			PathNode node=shortestPathNodes.elementAt(i);
			if(findInput==false){
				if(!(node==null)){
					if(node.getObjectNode()==input){
						findInput=true;
					}
				}
			}
			else if(findNextTable==false){
				if(!(node==null)){
					if(!(node.getSubjectNode()==null)){
						findNextTable=true;
					}
				}
			}
			else if(!(node==null)){
				if(node.getNodeType()==PathNode.ISAPRIMARYKEYCOLUMN){
					if(shortestPathNodes.elementAt(i+1).getNodeType()==PathNode.ISAPRIMARYKEYCOLUMN){
						firstRelationHasMultiplePrimaryKey=true;
					}
					i=shortestPathNodes.size();
				}
			}
		}
		return firstRelationHasMultiplePrimaryKey;
			
	}

	/**retrieve the table containing the input column
	 * 
	 * @param inputColumn
	 * @return Resource input column
	 */
	private Resource getInputTable(Resource inputColumn){
		Resource inputTable=null;
		for(int i=0;i<path.size();i++){
			if(path.elementAt(i).getObjectNode().equals(inputColumn)){
				while(!(path.elementAt(i).getNodeType()==PathNode.ISATABLE)){
					i++;
				}
				inputTable=path.elementAt(i).getObjectNode();
				i=path.size();
			}
		}
		return inputTable;
	}
	
	/**search the output table**/
	private void getOutputTable(){
		for(int i=0;i<path.size();i++){
			if(path.elementAt(i).getObjectNode().equals(output)){
				if(path.elementAt(i).getNodeType()==PathNode.ISAPRIMARYKEYCOLUMN){
					if(path.elementAt(i-1).getNodeType()==PathNode.ISAPRIMARYKEYCOLUMN){
						outputType=OUTPUTISMULTIPLEPK;
					}
					else if(path.elementAt(i+1).getNodeType()==PathNode.ISAPRIMARYKEYCOLUMN){
						outputType=OUTPUTISMULTIPLEPK;
					}
					else{
						outputType=OUTPUTISSIMPLEPK;
					}
					while(!(path.elementAt(i).getNodeType()==PathNode.ISATABLE)){
						i++;
					}
				}
				else{
					while(!(path.elementAt(i).getNodeType()==PathNode.ISATABLE)){
						i--;
					}
				}
				outputTable=path.elementAt(i).getObjectNode();
				i=path.size();
			}
		}
	}
	
	/**
	 * create a vector containing all inheritance relationship
	 * @param pathnode
	 * @return Vector<Pathnode>
	 */
	private Vector<PathNode> detectHeritageRelationships(Vector<PathNode> pathnode){
		Vector<PathNode> resourceVector=new Vector<PathNode>();
		for(int i=0;i<pathnode.size();i++){
			
			if(pathnode.elementAt(i).getRelationType()==PathNode.ISHERITAGERELATION){
				resourceVector.add(pathnode.elementAt(i));
			}
		}
		return resourceVector;
	}
	
	/**
	 * create a SPARQL query from a shortest path vector created with the findShortestPath() method
	 * @return Vector<String> each String is a line of the SPARQL query
	 */
	private Vector<String> buildRequest(){
		shortestPathNodes=invertShortestPathNodes(shortestPathNodes);
		if(DEBUG==true){
			System.out.println("finish invertShortestPath");
			for(int i=0;i<shortestPathNodes.size();i++){
				System.out.println(shortestPathNodes.elementAt(i));
			}
		}
//			shortestPathNodes.add(0, null);
		deleteDoubleNull();
		if(!shortestPathNodes.isEmpty()){
		verifyNull();
		Vector<String> prebuildRequest=preBuildRequest();
		if(DEBUG==true){
			System.out.println("finish prebuildrequest");
			for(int i=0;i<prebuildRequest.size();i++){
				System.out.println(prebuildRequest.elementAt(i));
			}
		}
		Vector<BlocRequete> blocRequeteVector=buildBlocRequeteVector(prebuildRequest);
		if(DEBUG==true){
			System.out.println("finish blocRequeteVector");
			for(int i=0;i<blocRequeteVector.size();i++){
				BlocRequete br=blocRequeteVector.elementAt(i);
				Vector<String> b=br.getBloc();
				System.out.println("level "+br.getBlocLevel());
				System.out.println("type "+br.getBlocType());
				System.out.println("first pred: "+br.getFirstPredicate());
				for(int j=0;j<b.size();j++){
					System.out.println(b.elementAt(j));
				}
			}
		}
		request=manageRequest(blocRequeteVector);
		if(DEBUG==true){
			System.out.println("finish manageRequest");
			for(int i=0;i<request.size();i++){
				System.out.println(request.elementAt(i));
			}
		}
		
		}
		else if(shortestPathNodes.isEmpty()){
			if(inputTable.equals(outputTable)){
				simpleQuery();
			}
			
		}
		namespaceToPrefix();
		postmodification();
		addNamespaces();
		return request;
	}
	
	//create query when input and output are in the same table
	private void simpleQuery(){
		String select="SELECT DISTINCT ?"+input.getLocalName()+" ?"+output.getLocalName()+" WHERE {";
		request.add(select);
		String filter="FILTER (?"+input.getLocalName()+"=\"inputString\").";
		request.add(filter);
		String tripleInput="?pk "+inputAnnot+" ?"+input.getLocalName()+".";
		request.add(tripleInput);
		String tripleOutput="?pk "+outputAnnot+" ?"+output.getLocalName()+".";
		request.add(tripleOutput);
//		ParseD2RQ modelToParse=new ParseD2RQ();
//		Model model=ModelFactory.createDefaultModel();
//		model.read(new File(mappingPath).toURI().toString(),"N3");
//		modelToParse.setModel(model);
//		Vector<Statement> infosInputTable=modelToParse.infosSurUneTable(inputTable);
//		Iterator<Statement> it=infosInputTable.iterator();
//		while(it.hasNext()){
//			Statement colonne=it.next();
//			System.out.println(colonne);
//		}
//		inputAnnot
		request.add("}");
	}
	
	/**manage the different bloc of the query previously detecting in order to have a structured SPARQL query
	 * 
	 * @param blocRequeteVector
	 * @return
	 */
	private Vector<String> manageRequest(Vector<BlocRequete> blocRequeteVector){
		Vector<String> firstPredicates=new Vector<String>();
		Vector<String> lastPredicates=new Vector<String>();
		//MANAGE BLOCS AND THEIR LEVELS (add "UNION", "{", "}")
		while(blocRequeteVector.size()>1){
			int levelMax=-1;
			int position=-1;
			for(int i=0;i<blocRequeteVector.size();i++){
				if(blocRequeteVector.elementAt(i).getBlocLevel()>levelMax){
					levelMax=blocRequeteVector.elementAt(i).getBlocLevel();
					position=i;
				}
			}
			
			BlocRequete currentBlocRequete=blocRequeteVector.elementAt(position);
			BlocRequete nextBlocRequete=blocRequeteVector.elementAt(position+1);
			//si le bloc suivant est du même niveau que le bloc actuel
			if(currentBlocRequete.getBlocLevel()==nextBlocRequete.getBlocLevel()){
				Vector<String> bloc=new Vector<String>();
				if(firstPredicates.size()==0){
					firstPredicates.add(currentBlocRequete.getFirstPredicate().toString());
					lastPredicates.add(currentBlocRequete.getLastPredicate().toString());
				}
				bloc.addAll(currentBlocRequete.getBloc());
				bloc.add("}");
				bloc.add("UNION");
				bloc.add("{");
				bloc.addAll(nextBlocRequete.getBloc());
				firstPredicates.add(nextBlocRequete.getFirstPredicate().toString());
				lastPredicates.add(nextBlocRequete.getLastPredicate().toString());
//				for(int i=0;i<bloc.size();i++){
//					System.out.println(bloc.elementAt(i));
//				}
				blocRequeteVector.removeElementAt(position);
				blocRequeteVector.removeElementAt(position);
				blocRequeteVector.insertElementAt(new BlocRequete(bloc, 0,levelMax), position);
			}
			//si le bloc suivant est de nv inférieur
			else{
				//si le bloc actuel est un union de portions de requête on recherche dans quelle portion doit s'integrer la portion suivante
				if(currentBlocRequete.getBloc().contains("UNION")){
					union=true;
					Vector<String> bloc=new Vector<String>();
					boolean findFirstPredicate=false;
					boolean findLastPredicate=false;
					for(int j=0;j<firstPredicates.size();j++){
						bloc=new Vector<String>();
						String firstPredicate=firstPredicates.elementAt(j);
						String lastPredicate=lastPredicates.elementAt(j);
						for(int i=0;i<nextBlocRequete.getBloc().size();i++){
							if(findFirstPredicate==false){
								String currentPredicate=nextBlocRequete.getBloc().elementAt(i);
								String []triplet=currentPredicate.split(" ");
								currentPredicate=triplet[1];
								if(currentPredicate.equals(firstPredicate.toString())){
									findFirstPredicate=true;
									bloc.add("{");
									bloc.addAll(currentBlocRequete.getBloc());
									bloc.add("}");
								}
								else{
									bloc.add(nextBlocRequete.getBloc().elementAt(i));
								}
							}
							
							else if(findLastPredicate==false){
								String currentPredicate=nextBlocRequete.getBloc().elementAt(i);
								String [] triplet=currentPredicate.split(" ");
								currentPredicate=triplet[1];
								if(currentPredicate.equals(lastPredicate)){
									findLastPredicate=true;
								}	
							}
							else{
//								bloc.add(nextBlocRequete.getBloc().elementAt(i));
								j=firstPredicates.size();
							}
						}
					}	
//					System.out.println(firstPredicate+"  "+lastPredicate);
//					blocRequeteVector.remove(position);
					blocRequeteVector.removeElementAt(position);
					blocRequeteVector.removeElementAt(position);
					blocRequeteVector.insertElementAt(new BlocRequete(bloc, 0,levelMax-1), position);
					firstPredicates.clear();
				}
				else{
					blocRequeteVector.remove(position);
				}
				
			}
		}
		//END OF BLOCS MANAGING
		
		//NOW NOW MODIFY QUERY (add SELECT, FILTER, first and last triple)
		Vector<String> request=blocRequeteVector.elementAt(0).getBloc();
//		System.out.println();
		//find and retrieve the first relation
		int firstRelationType=getFirstRelation();
		//add SELECT line of the query
		String select="SELECT DISTINCT ?"+input.getLocalName()+" ?"+output.getLocalName()+" WHERE {";
		request.add(0,select);
		//add FILTER line of the query
		String filter="FILTER (?"+input.getLocalName()+"=\"inputString\").";
//		String filter="FILTER regex(?"+input.getLocalName()+",\"^inputString$\").";
		request.add(1, filter);
		//if query contains "UNION", add first triplet of the query
		if(union==true){
			String[] triplet=request.elementAt(2).split(" ");
			String subject=triplet[0];
			String predicate=inputAnnot.toString();
			String object=triplet[2];
			request.remove(2);
//			System.out.println(subject+" "+predicate+" "+object);
			request.add(2, subject+" "+predicate+" "+object);
		}
		//if query don't contains "UNION
		else if(union==false){
			//si la premiere relation est une relation de clé etrangere inverse
			if(firstRelationType==PathNode.ISANINVERSEFOREIGNKEYCOLUMN){
				String subject="";
				String[] triplet=request.elementAt(2).split(" ");
				if(!firstRelationHasMultiplePrimaryKey()){
					subject=triplet[0];
					request.remove(2);
				}
				else{
					Character firstChar=request.elementAt(2).charAt(0);
					if(firstChar=='+'){
						subject=triplet[2].substring(0, triplet[2].length()-1);
					}
					else if(firstChar=='-'){
						subject=triplet[0].substring(1, triplet[0].length());
					}
					else{
						subject=triplet[0];
						request.remove(2);
					}
				}
				String predicate=inputAnnot.toString();
				String object="?"+input.getLocalName();
				request.add(2, subject.substring(0, subject.length())+" "+predicate+" "+object+".");
			}
			//si la premiere relation est une relation de clé etrangere
			else if(firstRelationType==PathNode.ISAFOREIGNKEYCOLUMN){
				String subject="";
				String[] triplet=request.elementAt(2).split(" ");
				if(!firstRelationHasMultiplePrimaryKey()){
					subject=triplet[0];
					request.remove(2);
				}
				else{
					Character firstChar=request.elementAt(2).charAt(0);
					if(firstChar=='+'){
//						System.out.println("+");
						subject=triplet[2].substring(0, triplet[2].length()-1);
					}
					else if(firstChar=='-'){
//						System.out.println("-");
						subject=triplet[0].substring(1, triplet[0].length());
					}
					else{
//						System.out.println(":");
						subject=triplet[0];
						request.remove(2);
					}
				}
				String predicate=inputAnnot.toString();
				String object="?"+input.getLocalName();
				request.add(2, subject.substring(0, subject.length())+" "+predicate+" "+object+".");
			}
			//modify the last line of the query
			int lastrel=getLastRelation();
			
			//si le dernière table possède une clé primaire composée d'un seul attribut
			if(outputType==0){
//				System.out.println("simplePK");
				String subject="";
				String []triplet=request.lastElement().split(" ");
				String object="";
				String predicate="";
				
				//si la dernière relation est du type ifk
				if(lastrel==5){

					if(lastPKWithoutAtt!=null){
						subject="?"+lastPKWithoutAtt.getLocalName();
					}
					else{
						request.remove(request.size()-1);
						subject=triplet[2];
						subject=subject.substring(0, subject.length()-1);
					}
				}
				
				//si la dernière relation est du type fk
				else if(lastrel==4){
//					System.out.println("fk");
					//find if the triple before the last has the same subject than last triple
					//if thier are equals, object must be object of the previous triple, else object must be the subject of the last triple
					String []beforeLastTriple=request.elementAt(request.size()-2).split(" ");
					String beforeLastSubject=beforeLastTriple[0];
					object=triplet[0];
					if(object.charAt(0)=='+'){
						object=object.substring(1);
					}
					if(beforeLastSubject.equals(object)){
						object=triplet[2].substring(0,triplet[2].length()-1);//);
					}
					predicate=lastRelation.toString();
					subject="?"+lastRelation.getLocalName();
					request.add(subject+" "+predicate+" "+object+".");
//					System.out.println(subject+" "+predicate+" "+object+".");
					triplet=request.lastElement().split(" ");
					if(lastPKWithoutAtt!=null){
						subject="?"+lastPKWithoutAtt.getLocalName();
					}
					else{
						subject=triplet[0];
					}
				}
//				System.out.println(shortestPathNodes.lastElement().getRelationType());
				if(shortestPathNodes.lastElement().getRelationType()!=2&&shortestPathNodes.lastElement().getRelationType()!=3){
					predicate=outputAnnot.toString();
				}
				else{
					//PROBLEME POUR SELECTIONNER LE PREDICATE DE SORTIE DANS LE CAS OU LA SORTIE EST UNE CLE PRIMAIRE
					predicate=output.toString();
				}
				object="?"+output.getLocalName();
				request.add(subject+" "+predicate+" "+object+".");
//				System.out.println(output);
				//verify if ouputTable and the table associated to output are the same
				ParseD2RQ modelToParse=new ParseD2RQ();
				Model model=ModelFactory.createDefaultModel();
				model.read(new File(mappingPath).toURI().toString(),"N3");
				modelToParse.setModel(model);
				Resource tableContainingOutput=modelToParse.columnIsInTable(output);
//				System.out.println(tableContainingOutput.toString()+" -> "+outputTable);
				//outputTable not equals to real output table because output column is a foreign key of an other table to.
				if(!tableContainingOutput.equals(outputTable)){
					//modify object of the last triple of the query
					String outputPK= "?pk_"+tableContainingOutput.getLocalName();
//					System.out.println(outputPK);
					request.remove(request.size()-1);
					StmtIterator stmts=model.listStatements();//ToParse.getForeignKeys();//infosSurUneColonne(output);
					Vector<Statement> findPropertiesRefers=new Vector<Statement>();
					Vector<Statement> findPropertiesBelongs=new Vector<Statement>();
					while(stmts.hasNext()){
						Statement stmt=(Statement)stmts.next();
						if( stmt.getPredicate().getLocalName().equals("refersToClassMap")&&stmt.getObject().equals(tableContainingOutput)){
							findPropertiesRefers.add(stmt);
//							System.out.println(stmt);
						}
						else if(stmt.getPredicate().getLocalName().equals("belongsToClassMap")&&stmt.getObject().equals(outputTable)){
							findPropertiesBelongs.add(stmt);
//							System.out.println(stmt);
						}
					}
					Resource property=null;
					for (int i=0;i<findPropertiesRefers.size();i++){
						for (int j=0;j<findPropertiesBelongs.size();j++){
							if(findPropertiesRefers.elementAt(i).getSubject().equals(findPropertiesBelongs.elementAt(j).getSubject())){
								property=findPropertiesRefers.elementAt(i).getSubject();
//								System.out.println(findPropertiesRefers.elementAt(i).getSubject());
							}
						}
					}
					request.add(subject+" "+property+" "+outputPK+".");
					request.add(outputPK+" "+predicate+" "+object+".");
				}
				//if input resource is a primary key subject and object of the first predicate are the same.
				//also the subject of the first predicate must be changed like pk_table_name
				
				
				String []firstTriple=request.elementAt(2).split(" ");
				String firstSubject=firstTriple[0];
				String firstObject=firstTriple[2].substring(0,firstTriple[2].length()-1);
				if(firstSubject.equals(firstObject)){
					firstSubject="?pk_"+inputTable.getLocalName();
					request.remove(2);
					String newFirstTriple=firstSubject+" "+firstTriple[1]+" "+firstTriple[2];
					request.add(2, newFirstTriple);
					String []secondTriple=request.elementAt(3).split(" ");
					if(secondTriple[2].equals(firstTriple[2])){
						String newSecondTriple=secondTriple[0]+" "+secondTriple[1]+" "+firstSubject+".";
						request.remove(3);
						request.add(3, newSecondTriple);
					}
				}
				
			}
			
			//si le dernière table possède une clé primaire composée de plusieurs attributs
			else if(outputType==1){
//				System.out.println("multiplePK");
				String []triplet=request.lastElement().split(" ");
				String subject="?"+output.getLocalName();
				String predicate="vocab:"+triplet[0].substring(1);
				String object=triplet[0];
				request.add(subject+" "+predicate+" "+object);
			}
			
			
		}
		request.add("}");
		return request;//request;
	}

	/**create a bloc like query(each bloc correspond to a subquery linking input to output). One bloc is detected for each inheritance relationship
	 * If only one bloc is created, this bloc correspond to the query linking initial input annotation to output annotation
	 * 
	 * @param prebuildRequest
	 * @return Vector<BlocRequete> 
	 */
	private Vector<BlocRequete> buildBlocRequeteVector(Vector<String> prebuildRequest){
		Vector<BlocRequete> blocRequeteVector=new Vector<BlocRequete>();
		int blocType=BlocRequete.FIRSTBLOC;
		Vector<String> bloc=new Vector<String>();
		//if query contains one line
//		if(prebuildRequest.size()==1){
//			bloc.add(prebuildRequest.elementAt(0));
//			blocRequeteVector.add(new BlocRequete(bloc, blocType));
//		}
		//if query contains some lines
//		else{
		int begin=-1;
		if(outputType==0){
			begin=1;
		}
		else{
			begin=0;
		}
		//ATTENTION il y a peut être un soucis à commencer le compteur à 0
		for(int i=0;i<prebuildRequest.size();i++){
			if(prebuildRequest.elementAt(i)=="fin de bloc"){
				blocRequeteVector.add(new BlocRequete(bloc, blocType));
				bloc=new Vector<String>();
				blocType=BlocRequete.NEWLEVELBLOC;
			}	
			else if(prebuildRequest.elementAt(i)=="UNION"){
				blocRequeteVector.add(new BlocRequete(bloc, blocType));
				bloc=new Vector<String>();
				blocType=BlocRequete.SAMELEVELBLOC;
			}
			else{
				bloc.add(prebuildRequest.elementAt(i));
			}
			if(prebuildRequest.size()<i+2){
				blocRequeteVector.add(new BlocRequete(bloc, blocType));
			}
		}
//		}
		int[] blocLevel=new int[blocRequeteVector.size()];
		int currentBlocLevel=0;
		int minBlocLevel=0;
		for(int i=0;i<blocRequeteVector.size();i++){
			if(blocRequeteVector.elementAt(i).getBlocType()==BlocRequete.FIRSTBLOC){
				blocLevel[i]=currentBlocLevel;
			}
			if(blocRequeteVector.elementAt(i).getBlocType()==BlocRequete.SAMELEVELBLOC){
				blocLevel[i]=currentBlocLevel;
			}
			if(blocRequeteVector.elementAt(i).getBlocType()==BlocRequete.NEWLEVELBLOC){
				if(blocRequeteVector.size()>i+1){
					if(blocRequeteVector.elementAt(i).getBloc().size()>0){
					Resource firstPredicate=blocRequeteVector.elementAt(i).getFirstPredicate();
//					System.out.println(blocRequeteVector.elementAt(i).getBloc().size());
					Vector<String>nextBlocRequest=blocRequeteVector.elementAt(i+1).getBloc();
					boolean findPredicate=false;
					for(int j=0;j<nextBlocRequest.size();j++){
//						System.out.println(nextBlocRequest.elementAt(j)+" --> "+firstPredicate);
						String [] triplet=nextBlocRequest.elementAt(j).split(" ");
						if(triplet[1]==firstPredicate.toString()){
							findPredicate=true;
							j=nextBlocRequest.size();
						}
					}
					if(findPredicate==false){
						currentBlocLevel++;
						blocLevel[i]=currentBlocLevel;
					}
					else{
						currentBlocLevel--;
						if(currentBlocLevel<minBlocLevel){
							minBlocLevel=currentBlocLevel;
						}
						blocLevel[i]=currentBlocLevel;
					}
				}
					else{
						blocRequeteVector.remove(i);
						}
					}
				else{
					currentBlocLevel++;
					blocLevel[i]=currentBlocLevel;
				}
			}
		}
		int maxBlocLevel=0;
		if(blocLevel.length>1){
			maxBlocLevel=blocLevel[blocLevel.length-1];
			for(int i=blocLevel.length-1;i>=0;i--){
				blocRequeteVector.elementAt(i).setBlocLevel(maxBlocLevel-blocLevel[i]);
			}
		}
		else{
			blocRequeteVector.elementAt(0).setBlocLevel(0);
		}
		return blocRequeteVector;
	}
	
	private void deleteDoubleNull(){
		for(int i=0;i<shortestPathNodes.size();i++){
			if(shortestPathNodes.elementAt(i)==null){
				if(shortestPathNodes.elementAt(i+1)==null){
					shortestPathNodes.remove(i);
				}
			}
		}
	}
	
	private Vector<String> preBuildRequest(){
		Vector<String> prebuildRequest=new Vector<String>();
		double pathlength=-1;
		Resource type2=null;
		Resource type3=null;
		Resource type4=null;
		Resource type5=null;
		//prend true si on parcours une table d'association sans attributs
		boolean withoutAtt=false;
		int associationtype=-1;
		for(int i=0;i<shortestPathNodes.size();i++){
			//si on ne change pas de bloc d'héritage
			if(!(shortestPathNodes.elementAt(i)==null)){
				PathNode currentNode=shortestPathNodes.elementAt(i);
				//récupère le type de relation qu'on parcours (heritage, table d'association, table d'association sans attributs)
				if(currentNode.getRelationType()!=-1){
					associationtype=currentNode.getRelationType();
				}
				//si on ne passe pas à une autre requête du même bloc d'héritage
				if(pathlength<=currentNode.getWeight()){
					pathlength=currentNode.getWeight();
					//si on ne parcours pas une table d'association sans attributs
					if(associationtype!=3){
						//si le sujet est null
					if(currentNode.getSubjectNode()==null){
						
						if(pathlength<=0){
							//si on parcours une simple colonne
							if(currentNode.getNodeType()==2){
								type2=currentNode.getObjectNode();
							}
							//si on parcours une clé primaire
							
							else if(currentNode.getNodeType()==3){
								type3=currentNode.getObjectNode();
								//si clé primaire composée de + de 1 attributs
								if(shortestPathNodes.elementAt(i+1).getNodeType()==3){
									Vector<Resource>primaryKeys=new Vector<Resource>();
									primaryKeys.add(currentNode.getObjectNode());
									while(shortestPathNodes.elementAt(i+1).getNodeType()==3){
										i++;
										primaryKeys.add(shortestPathNodes.elementAt(i).getObjectNode());
									}
									type3=new ResourceImpl(currentNode.getObjectNode().getNameSpace()+"key_"+shortestPathNodes.elementAt(i+1).getSubjectNode().getLocalName());
	//								System.out.println("?"+type3.getLocalName()+" "+type2+" ?"+type2.getLocalName());
								}
								//si clé primaire composée d'un seul attribut
								else{
									prebuildRequest.add("?"+type3.getLocalName()+" "+type2+" ?"+type2.getLocalName()+".");
//									if(currentNode.getWeight()==0){
//	//									System.out.println("   ?"+type3.getLocalName()+" "+type2+" ?"+type2.getLocalName());
//									}
								}
							}
						}
					}
					else{
						
						//si on ne rencontre pas une table d'association sans attributs
						if(currentNode.getRelationType()!=3){
							//si on parcours une clé étrangère
							if(currentNode.getNodeType()==4){
								type4=currentNode.getObjectNode();
								if(shortestPathNodes.size()>i+4){
									//si clé primaire composée de plusieurs colonnes
									if(shortestPathNodes.elementAt(i+3).getNodeType()==3){
										
										if(withoutAtt==true){
											withoutAtt=false;
											lastPKWithoutAtt=null;
											if(shortestPathNodes.elementAt(i+2).getWeight()<pathlength){
												prebuildRequest.add("?"+type2.getLocalName()+" "+outputAnnot+" ?"+output.getLocalName()+".");
											}
//											i++;
										}
										//si on ne parcours pas le 2eme partie d'une clé primaire sans attributs
										else if(!(shortestPathNodes.elementAt(i+1).getRelationType()==3)){
											
//											System.out.println(i+"toto: "+shortestPathNodes.elementAt(i+1).getRelationType());
											prebuildRequest.add("?key_"+shortestPathNodes.elementAt(i+1).getObjectNode().getLocalName()+" "+type4+" ?"+type3.getLocalName()+".");
											int j=i+2;
											while(shortestPathNodes.elementAt(j).getNodeType()==3){
												type3=shortestPathNodes.elementAt(j).getObjectNode();
												j++;
											}
										}
//										else{
//											System.out.println();
//										}
									}
									//clé primaire composée d'une seule colonne
									else{
										prebuildRequest.add("?"+shortestPathNodes.elementAt(i+2).getObjectNode().getLocalName()+" "+type4+" ?"+type3.getLocalName()+".");
										type3=shortestPathNodes.elementAt(i+2).getObjectNode();
									}
								}
	//							variableNumber++;
							}
							//si on parcours une simple colonne 
							if(currentNode.getNodeType()==2){
								type2=currentNode.getObjectNode();
								if(shortestPathNodes.elementAt(i-2).getNodeType()==3){
									prebuildRequest.add("?key_"+shortestPathNodes.elementAt(i).getSubjectNode().getLocalName()+" "+type2+" ?"+type2.getLocalName()+".");
								}
								else{
									prebuildRequest.add("?"+type3.getLocalName()+" "+type2+" ?"+type2.getLocalName()+".");
								}
							}
							//si on parcours une clé étrangère inverse
							if(currentNode.getNodeType()==5){
								//si la table cible possède une clé primaire composée de plusieurs colonnes
								if(shortestPathNodes.size()<i+3){
									type5=currentNode.getObjectNode();
									prebuildRequest.add("?key_"+shortestPathNodes.elementAt(i+1).getObjectNode().getLocalName()+" "+type5+" "+"?"+type2.getLocalName()+".");
								}
								//si la table cible possède une clé primaire composée d'une seule colonne
								else if(shortestPathNodes.elementAt(i+2)==null||(shortestPathNodes.elementAt(i+2).getWeight()<pathlength)){
									type5=currentNode.getObjectNode();
									prebuildRequest.add("?"+type2.getLocalName()+" "+outputAnnot+" ?"+output.getLocalName()+".");
								}
								//quand on est dans aucun des 2 cas précédents
								else{
									type3=type2;
									type5=currentNode.getObjectNode();
								}
								
							}
						}

						}
//					if(currentNode.getObjectNode().getLocalName().equals("germplasmmicrosat")||currentNode.getObjectNode().getLocalName().equals("varietalstudy_id_study")||currentNode.getObjectNode().getLocalName().equals("varietalstudy_id_varietalstudy")||currentNode.getObjectNode().getLocalName().equals("varietalsituation_id_germplasm")||currentNode.getObjectNode().getLocalName().equals("varietalsituation_id_varietalstudy")){
//						System.out.println("test\t"+"cur "+currentNode.getObjectNode().getLocalName()+" "+type2+" "+type3+" "+type4+" "+type5+" "+pathlength+"\n");
//					}
					}
					//si on parcours une table d'association sans attributs
					else{
						withoutAtt=true;
						for(int j=0;j<awa.size();j++){
							if(awa.elementAt(j).getTable().equals(currentNode.getObjectNode())){
//							System.out.println("awa "+awa.elementAt(j).getTable()+" -> "+currentNode.getObjectNode());
//							System.out.println(shortestPathNodes.elementAt(i-2).getObjectNode()+" -> "+awa.elementAt(j).getRefersColumn());
							if(awa.elementAt(j).getRefersColumn().equals(shortestPathNodes.elementAt(i-2).getObjectNode())){
//								System.out.println("refers: "+awa.elementAt(j).getRefersColumn()+" -> "+currentNode.getObjectNode());
								lastPKWithoutAtt=awa.elementAt(j).getBelongsColumn();
								type3=awa.elementAt(j).getBelongsColumn();
//								System.out.println(type5);
//								System.out.println("type: "+type3);
								prebuildRequest.add(prebuildRequest.size(),"+"+"?"+awa.elementAt(j).getBelongsColumn().getLocalName()+" "+awa.elementAt(j).getTable()+" ?"+awa.elementAt(j).getRefersColumn().getLocalName()+".");
							}
							else if(awa.elementAt(j).getBelongsColumn().equals(shortestPathNodes.elementAt(i-2).getObjectNode())){
//								System.out.println("belongs: "+awa.elementAt(j).getBelongsColumn()+" -> "+currentNode.getObjectNode());
								prebuildRequest.add(prebuildRequest.size(),"-"+"?"+awa.elementAt(j).getBelongsColumn().getLocalName()+" "+awa.elementAt(j).getTable()+" ?"+awa.elementAt(j).getRefersColumn().getLocalName()+".");
//								System.out.println("type: "+type2);
								lastPKWithoutAtt=awa.elementAt(j).getRefersColumn();
								type3=awa.elementAt(j).getRefersColumn();
							}
							}
						}
//						int j=i+1;
//						while(shortestPathNodes.elementAt(j).getRelationType()!=0){
//							j++;
//						}
						/* correction d'une erreur le 2/10/11
						 * ajout de la ligne if(shortestpathnodes...) et de la clause else associée
						 */
						
						if(shortestPathNodes.elementAt(i+1).getRelationType()==0){
							if(shortestPathNodes.size()>i+2){
								if(shortestPathNodes.elementAt(i+2)==null){
									prebuildRequest.add("?"+type3.getLocalName()+" "+outputAnnot+" ?"+output.getLocalName()+".");
								}
								else if(shortestPathNodes.elementAt(i+2).getWeight()==0){
									prebuildRequest.add("?"+type3.getLocalName()+" "+outputAnnot+" ?"+output.getLocalName()+".");
								}
							}
//							else{
//								prebuildRequest.add("?"+type3.getLocalName()+" "+outputAnnot+" ?"+output.getLocalName()+".");
//							}
						}
					}
						
				}
				else{
					i--;
					prebuildRequest.add("UNION");
					pathlength=-1;
					withoutAtt=false;
					type2=null;
					type3=null;
					type4=null;
					type5=null;
				}
			}
			else{
				prebuildRequest.add("fin de bloc");
				pathlength=-1;
				withoutAtt=false;
				type2=null;
				type3=null;
				type4=null;
				type5=null;
			}
		}
//		for(int i=0;i<prebuildRequest.size();i++){
//			System.out.println(prebuildRequest.elementAt(i));
//		}
		return prebuildRequest;
	}
	
	/**change each namespace of the query to its correponding prefix.
	 * 
	 */
	private void namespaceToPrefix(){
		Model model=ModelFactory.createDefaultModel();
		model.read(new File(mappingPath).toURI().toString(),"N3");
//		
		
		for(int i=0;i<request.size();i++){
			String requestLine=request.elementAt(i);
			if(!(requestLine.equals("{")||requestLine.equals("}")||requestLine.equals("UNION")||requestLine.subSequence(0, 6).equals("FILTER")||requestLine.subSequence(0, 6).equals("SELECT"))){
				String[] triplet=requestLine.split(" ");
				String subject=triplet[0];
				Resource predicateResource=new ResourceImpl(triplet[1]);
				String object=triplet[2];
				String predicate=model.getNsURIPrefix(predicateResource.getNameSpace());
//				System.out.println(predicate);
				if(!(predicate==null)){
					if(predicate.equals("map")){
						predicate="vocab:"+predicateResource.getLocalName();
					}
					else{
						predicate+=":"+predicateResource.getLocalName();
					}
				}
				else{
					Map<String, String> nsPrefixMap=model.getNsPrefixMap();
					Collection<String> values=nsPrefixMap.values();
					Collection<String> keys=nsPrefixMap.keySet();
					Iterator<String> value=values.iterator();
					Iterator<String> key=keys.iterator();
					while(value.hasNext()){
						String ns=value.next();
						String prefixe=key.next();
						String predName=predicateResource.toString();
						if(predName.length()>=ns.length()){
//							System.out.println(predName.substring(0,ns.length())+" -> "+ns.toString());
							if(predName.substring(0,ns.length()).equals(ns)){
//								System.out.println(prefixe+":"+predName.substring(ns.length()));
								predicate=prefixe+":"+predName.substring(ns.length());
							}
						}
					}
				}
				request.remove(i);
				request.add(i, subject+" "+predicate+" "+object);
//				System.out.println(nsPrefixMap.get(predicateResource.getNameSpace()));
			}
			
		}
		model.close();
	}
	/**declare all needed namespaces at the beginning of the query
	 * 
	 */
	private void addNamespaces(){
		Model model=ModelFactory.createDefaultModel();
		model.read(new File(mappingPath).toURI().toString(),"N3");
		Map<String, String> nsPrefix=model.getNsPrefixMap();
		ArrayList<String> nsToAdd= new ArrayList<String>();
		for(int i=0;i<request.size();i++){
			if(request.elementAt(i).charAt(0)=='?'){
				String[] triplet=request.elementAt(i).split(" ");
				String predicate=triplet[1];
				predicate=predicate.substring(0, predicate.indexOf(':'));
				String NsTemp="PREFIX "+predicate+": <"+nsPrefix.get(predicate)+">";
				if(!nsToAdd.contains(NsTemp)){
					nsToAdd.add(NsTemp);
				}
			}
		}
		for(int i=0;i<nsToAdd.size();i++){
			request.add(0, nsToAdd.get(i));
		}
	}
	
	/**invert all elements of the vector containing the shortest path
	 * 
	 * @param spn intial shortest path
	 * @return Vector<PathNode> invert shortest path
	 */
	private Vector<PathNode> invertShortestPathNodes(Vector<PathNode> spn){
		Vector<PathNode> vectinv=new Vector<PathNode>();
		for(int i=shortestPathNodes.size()-1;i>=0;i--){
			vectinv.add(shortestPathNodes.elementAt(i));
		}
		return vectinv;
	}
	
	/**postmodify query in case of UNION and delete association table orientation (+ and - at beginning of a line)
	 * 
	 */
	private void postmodification(){
		Stack<String> pile=new Stack<String>();
		//modify query in case of UNION
		for(int i=0;i<request.size();i++){
			String line=request.elementAt(i);
			if(line.equals("{")){
				if(request.elementAt(i-1).equals("UNION")){
					String nextline=request.elementAt(i+1);
					String [] triplet=nextline.split(" ");
					String newtriplet=triplet[0]+" "+triplet[1]+" "+pile.peek()+".";
					request.remove(i+1);
					request.add(i+1, newtriplet);
				}
				else{
					String linebefore=request.elementAt(i-1);
					String [] triplet=linebefore.split(" ");
					String element=triplet[0];
					pile.push(element);
					String lineafter=request.elementAt(i+1);
					triplet=lineafter.split(" ");
					String newtriplet=triplet[0]+" "+triplet[1]+" "+pile.peek()+".";
					request.remove(i+1);
					request.add(i+1, newtriplet);
				}
			}
			else if(line.equals("}")&&i+1<request.size()){
				if(!request.elementAt(i+1).equals("UNION")){
					pile.pop();
				}
			}
			//delete informations about association table orientation
			else if(request.elementAt(i).charAt(0)=='-'||request.elementAt(i).charAt(0)=='+'){
				String queryline=request.elementAt(i);
				request.remove(i);
				queryline=queryline.substring(1);
				request.add(i, queryline);
			}
		}
	}
	private void verifyNull(){
		if(shortestPathNodes.elementAt(0)!=null){
			shortestPathNodes.add(0,null);
		}
	}
}
