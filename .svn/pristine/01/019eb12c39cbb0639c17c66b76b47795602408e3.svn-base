package com.wscreation.main;

import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.wscreation.repository.FindQueryForAllMappingFiles;
import com.wscreation.repository.QueryCorrepondances;

public class FindPathsMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String mappingDir="d:\\workspace2\\WScreation";
//		String mappingPath="mapping-TG_LOMAGNE_RICE_final.n3";
//		FindOnePath path= new FindOnePath(mappingPath);
		//input concept
		Resource inputR=new ResourceImpl("http://gcpdomainmodel.org/GCPDM#GCP_Study");
//		Resource inputR=new ResourceImpl("http://edamontology.org/data_0966");
//		Resource inputR=new ResourceImpl("http://purl.bioontology.org/ontology/CO#accession_number");
		//output concept
//		Resource outputR=new ResourceImpl("http://edamontology.org/data_0842");
		Resource outputR=new ResourceImpl("http://gcpdomainmodel.org/GCPDM#GCP_GenomicFeatureDetector");
//		Resource outputR=new ResourceImpl("http://www.w3.org/2003/01/geo/wgs84_pos#longitude");
//		Resource outputR=new ResourceImpl("http://purl.org/dc/elements/1.1/date");
		FindQueryForAllMappingFiles queries=new FindQueryForAllMappingFiles(mappingDir,inputR,outputR);
		Vector<QueryCorrepondances>q=queries.getQueryCorres();
		for(int i=0;i<q.size();i++){
			QueryCorrepondances qc=q.elementAt(i);
			System.out.println(qc.getMappingFile());
			Vector<String>s=qc.getQuery();
			for (int j=0;j<s.size();j++){
				System.out.println(s.elementAt(j));
			}
		}
	}

}
