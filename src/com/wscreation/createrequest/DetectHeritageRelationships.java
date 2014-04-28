package com.wscreation.createrequest;

import java.util.Vector;

import com.wscreation.findpath.PathNode;

public class DetectHeritageRelationships {
	
	public Vector<PathNode> detect(Vector<PathNode> pathnode){
		Vector<PathNode> resourceVector=new Vector<PathNode>();
		for(int i=0;i<pathnode.size();i++){
			if(pathnode.elementAt(i).getRelationType()==PathNode.ISHERITAGERELATION){
				resourceVector.add(pathnode.elementAt(i));
			}
		}
		return null;
	}

}
