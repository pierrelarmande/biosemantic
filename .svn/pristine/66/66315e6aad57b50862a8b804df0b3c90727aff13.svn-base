package com.wscreation.createrequest;

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class BlocRequete {
	
	//ATTRIBUTS
	private Vector<String> bloc=new Vector<String>();
	private int blocLevel;
	private Resource firstPredicate=null;
	private Resource lastPredicate=null;
	private int blocType;
	static int FIRSTBLOC=0;
	static int NEWLEVELBLOC=1;
	static int SAMELEVELBLOC=2;
	
	//CONSTRUCTOR
	public BlocRequete(Vector<String> bloc,int blocType) {
		this.bloc=bloc;
		this.blocType=blocType;
		getPredicate(1);
		getPredicate(2);
	}
	
	public BlocRequete(Vector<String> bloc,int blocType,int blocLevel) {
		this.bloc=bloc;
		this.blocLevel=blocLevel;
//		System.out.println("this: "+this.bloc.size()+ " -> "+bloc.size());
		this.blocType=blocType;
		getPredicate(1);
		getPredicate(2);
	}
	
	//ACCESSOR
	public void setBlocLevel(int blocLevel){this.blocLevel=blocLevel;}
	public int getBlocLevel(){return blocLevel;}
	public Resource getFirstPredicate(){return firstPredicate;}
	public Resource getLastPredicate(){return lastPredicate;}
	public void setBlocType(int blocType){this.blocType=blocType;}
	public int getBlocType(){return blocType;}
	public Vector<String> getBloc(){return bloc;}
	
	
	//METHODS
	/**get the first or the last predicate
	 * @param elementOfThePredicate an integer. value=1 for first predicate and 2 for last predicate
	 * @return void directly modify class attribut firstpredicate
	 */
	private void getPredicate(int elementOfThePredicate){
		if(bloc.size()>0){
//		System.out.println("piou :"+bloc.elementAt(1));
		
		if(elementOfThePredicate==1){
			String element=bloc.elementAt(0);
			String[] triplet=element.split(" ");
			firstPredicate=new ResourceImpl(triplet[1]);
		}
		else if(elementOfThePredicate==2){
			if(bloc.size()>2){
				String element=bloc.elementAt((bloc.size()-2));
				String[] triplet=element.split(" ");
				lastPredicate=new ResourceImpl(triplet[1]);
			}
			else{
				String element=bloc.elementAt((bloc.size()-1));
				String[] triplet=element.split(" ");
				lastPredicate=new ResourceImpl(triplet[0]);
			}
			
		}
		}
	}
	
	
}
