package codemap1;

import com.tinkerpop.blueprints.Direction;
import static com.tinkerpop.blueprints.Direction.OUT;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphFactory;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 *
 * @author mdunsdon
 */
public class tinkerTest {

    private Graph graph;
    
    tinkerTest() {
        graph = new TinkerGraph();
        //Graph g = GraphFactory.open("graph.properties")
    }

	public Vertex addVertex(String vertexName, String vertexProperty){
        	Vertex a = graph.addVertex(null);
        	a.setProperty(vertexName, vertexProperty);
		return a;
	}
	public void addEdge(Vertex vertFrom, Vertex vertTo, String edgeLabel){
        	Edge e = graph.addEdge(null, vertFrom, vertTo, edgeLabel);
//	        System.out.println(e.getVertex(Direction.OUT).getProperty("name") + "--" + e.getLabel() + "-->" + e.getVertex(Direction.IN).getProperty("name"));
       
	}
   	
	public void printVertexEdges(String vertexLabel){
		Vertex a = graph.getVertex(vertexLabel);
		System.out.println("vertex " + a.getId() + " has name " + a.getProperty("name"));
		for(Edge e : a.getEdges(OUT)) {
		  System.out.println(e);
		}
	}

	public Vertex getVertex(String vertexLabel){
		return graph.getVertex(vertexLabel);
	}
	public void dumpGraph() {
		  System.out.println("Vertices of " + graph);
		  for (Vertex vertex : graph.getVertices()) {
		    System.out.println(vertex);
		  }
		  System.out.println("Edges of " + graph);
		  for (Edge edge : graph.getEdges()) {
		    System.out.println(edge);
		   }
		}
}
