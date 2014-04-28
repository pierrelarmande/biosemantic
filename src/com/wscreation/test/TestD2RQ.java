package com.wscreation.test;
//
//import java.io.File;
//import java.util.Vector;
//
//
//
//import com.hp.hpl.jena.query.Query;
//import com.hp.hpl.jena.query.QueryExecutionFactory;
//import com.hp.hpl.jena.query.QueryFactory;
//import com.hp.hpl.jena.query.QuerySolution;
//import com.hp.hpl.jena.query.ResultSet;
//import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.ModelFactory;
//import com.wscreation.d2rqmappingfile.Writter;
//
//import de.fuberlin.wiwiss.d2rq.ModelD2RQ;
//import de.fuberlin.wiwiss.d2rq.sesame.D2RQRepository;
//import de.fuberlin.wiwiss.d2rq.sesame.D2RQSource;
//
public class TestD2RQ {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		System.out.println(System.getProperty("user.dir"));
//		String studyName="haploryza";
//		String fileName="mapping-TG__LOCAL_RICE.n3";
//		String dirPath="http://localhost:8080/d2rq_repository/";
//		ModelD2RQ model=new ModelD2RQ(dirPath+fileName);
//		
////		D2RQSource m = new D2RQSource(");
//		D2RQSource source = new D2RQSource("file:///where/you/stored/the/d2rq-mapping.n3", "N3",null);
////	    Repository repos = new D2RQRepository("urn:youRepository", source, SESAME.class);
//
//	    // Query the repository
////	    String query = "SELECT ?x, ?y WHERE (<http://www.conference.org/conf02004/paper#Paper1>, ?x, ?y)";        
////	    QueryResultsTable result = repos.performTableQuery(QueryLanguage.RDQL, query);
////
////	    // print the result
////	    int rows = result.getRowCount();
////	    int cols = result.getColumnCount();
////	    for(int i = 0; i < rows; i++){
////	        for(int j = 0; j < cols; j++){
////	            Value v = result.getValue(i,j);
////	            System.out.print(v.toString() + "    ");                  
////	        }
////	        System.out.println();
////	    }
//
////		HTTPRepository repo=new HTTPRepository("http://gohelle.cirad.fr:8080/openrdf-sesame/repositories/d2rq_repo");
////		repo.toString();
////		URI context=repo.getValueFactory().createURI("http://lomagne.cirad.fr/vocab/resource/");
//		
//			
////		RepositoryResult<Statement> result = con.getStatements(null, null, null, true, context);
////		ModelD2RQ m = new ModelD2RQ(mappingFile);
//		
//		String sparql1 = 
//			"PREFIX gcpdm: <http://gcpdomainmodel.org/GCPDM#>"+
//			"PREFIX vocab: <http://lomagne.cirad.fr/vocab/resource/>"+
//			"SELECT DISTINCT ?study_id ?study_name ?germplasm_name WHERE {"+
//			"?study_id gcpdm:GCP_SimpleIdentifier ?study_name."+
//			"FILTER regex(?study_name,\"^"+studyName.toString()+"$\")."+
//			"?genotypestudy_id vocab:genotypingstudy_id_study ?study_id."+
//			"?key vocab:dnasamplegenotypingstudy_id_genotypingstudy ?genotypestudy_id."+
//			"?key vocab:dnasamplegenotypingstudy_id_dnasample ?dnasample."+
//			"?dnasample vocab:dnasample_id_germplasm ?germplasm_id."+
//			"?germplasm_id gcpdm:GCP_Germplasm ?germplasm_name."+
//			"}";
//		Query q1 = QueryFactory.create(sparql1); 
//		ResultSet rs1;
////		Model model=ModelFactory.createDefaultModel();
////		//test with jenasesame API (openjena)
////		try{
////			RepositoryConnection con=repo.getConnection();
////			model=JenaSesame.createModel(con);
////		}
////		catch(RepositoryException e){System.out.println(e.getMessage());}
//		//test with JenaSesame API
//
////		ModelD2RQ mod=new ModelD2RQ(model, null);
////		System.out.println(mod.toString());
//		rs1 = QueryExecutionFactory.create(sparql1, model).execSelect();
//		Vector<String> dnaSamplesNames=new Vector<String>();
//		while (rs1.hasNext()) {
//		    QuerySolution row = rs1.nextSolution();
//		    dnaSamplesNames.add(
//		    		"<gcpdm:GCP_Germplasm/>"+row.getLiteral("germplasm_name").getString()+"<gcpdm:GCP_SimpleIdentifier/>"+row.getLiteral("study_name").getString());
//		    System.out.println("<gcpdm:GCP_Germplasm/>"+row.getLiteral("germplasm_name").getString()+"<gcpdm:GCP_SimpleIdentifier/>"+row.getLiteral("study_name").getString());
//		}
//		System.out.println(dnaSamplesNames.size());
//	}
//
}
