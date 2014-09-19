/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codemap1;

import java.util.List;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.query.And;
import org.hypergraphdb.query.AtomPartCondition;
import org.hypergraphdb.query.AtomTypeCondition;
import org.hypergraphdb.query.ComparisonOperator;
import org.hypergraphdb.query.HGQueryCondition;

/**
 *
 * @author mdunsdon
 */
public class hgClass {
	private HyperGraph graph;
	public hgClass(String dbLocation){
		graph = new HyperGraph(dbLocation);
	}

	public void closeGraph(){
		graph.close();
	}
	public HGHandle addVertex(String vertexName)	{
		return graph.add(vertexName);
	}
	public HGHandle addVertex(vbFileClass vbFile)	{
		return graph.add(vbFile);
	}
	public HGHandle addVertex(methodClass meth)	{
		return graph.add(meth);
	}

	public HGHandle findVertex(String vertexLabel, Class<?> javaClass){
		HGHandle result=null;
		HGQueryCondition condition = new And(
              		new AtomTypeCondition(javaClass), 
                	new AtomPartCondition(new String[]{"vertexLabel"}, vertexLabel, ComparisonOperator.EQ));
	    	HGSearchResult<HGHandle> rs = graph.find(condition);
		    try
		    {
			while (rs.hasNext())
			{
			    result = rs.next();
//			    Book book = graph.get(current);
//			    System.out.println(book.geTitle());
			}
		    }
		    finally
		    {
			rs.close();
		    }
		return result;
	}

	public void dump(){
		List<vbFileClass> vbFiles = hg.getAll(graph,hg.type(vbFileClass.class));
		System.out.println("VB Files");
		for(vbFileClass vb:vbFiles){
			System.out.println(vb.getVertexLabel());

			List<methodClass> methodList = hg.getAll(graph, hg.and(hg.type(methodClass.class),hg.link(vb)));
			System.out.println("\tMethods");
			for(methodClass meth:methodList){
				System.out.println("\t");
				System.out.println(meth.getVertexLabel());
			}
		}
		

		System.out.println("Links");
		
		
	}
}
