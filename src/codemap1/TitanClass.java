package codemap1;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.INDEX_BACKEND_KEY;
import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY;
import com.tinkerpop.blueprints.Vertex;
import java.io.File;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mdunsdon
 */
public class TitanClass {
    private TitanGraph g;
//    public TitanGraph getGraph(){
//        g = TitanFactory.open("/local/XIF");
//        return g;
//    }
    
    
    
    public static TitanGraph getGraph(final String directory) {
        BaseConfiguration config = new BaseConfiguration();
        Configuration storage = config.subset(GraphDatabaseConfiguration.STORAGE_NAMESPACE);
        // configuring local backend
        storage.setProperty(GraphDatabaseConfiguration.STORAGE_BACKEND_KEY, "local");
        storage.setProperty(GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY, directory);
        // configuring elastic search index
//        Configuration index = storage.subset(GraphDatabaseConfiguration.INDEX_NAMESPACE).subset(INDEX_NAME);
//        index.setProperty(INDEX_BACKEND_KEY, "elasticsearch");
//        index.setProperty("local-mode", true);
//        index.setProperty("client-only", false);
//        index.setProperty(STORAGE_DIRECTORY_KEY, directory + File.separator + "es");

        TitanGraph graph = TitanFactory.open(config);
        //TitanClass.load(graph);
        
        graph.makeKey("filePath").dataType(String.class).indexed(Vertex.class).unique().make();
        graph.makeKey("methodName").dataType(String.class).indexed(Vertex.class).unique().make();
        graph.makeKey("calls").dataType(String.class).indexed(Vertex.class).unique().make();
//        graph.makeKey("age").dataType(Integer.class).indexed(INDEX_NAME, Vertex.class).make();
//        graph.makeKey("type").dataType(String.class).make();

        //final TitanKey time = graph.makeKey("time").dataType(Integer.class).make();
        //final TitanKey reason = graph.makeKey("reason").dataType(String.class).indexed(INDEX_NAME, Edge.class).make();
       // graph.makeKey("place").dataType(Geoshape.class).indexed(INDEX_NAME, Edge.class).make();

//        graph.makeLabel("father").manyToOne().make();
//        graph.makeLabel("mother").manyToOne().make();
//        graph.makeLabel("battled").sortKey(time).make();
//        graph.makeLabel("lives").signature(reason).make();
//        graph.makeLabel("pet").make();
//        graph.makeLabel("brother").make();

        graph.commit();
        
        
        return graph;
    }
    
//     public static void createGraph(final TitanGraph graph) {
//
//       
//
//        // vertices
//
//        Vertex saturn = graph.addVertex(null);
//        saturn.setProperty("name", "saturn");
//        saturn.setProperty("age", 10000);
//        saturn.setProperty("type", "titan");
//
//        Vertex sky = graph.addVertex(null);
//        ElementHelper.setProperties(sky, "name", "sky", "type", "location");
//
//        Vertex sea = graph.addVertex(null);
//        ElementHelper.setProperties(sea, "name", "sea", "type", "location");
//
//        Vertex jupiter = graph.addVertex(null);
//        ElementHelper.setProperties(jupiter, "name", "jupiter", "age", 5000, "type", "god");
//
//        Vertex neptune = graph.addVertex(null);
//        ElementHelper.setProperties(neptune, "name", "neptune", "age", 4500, "type", "god");
//
//        Vertex hercules = graph.addVertex(null);
//        ElementHelper.setProperties(hercules, "name", "hercules", "age", 30, "type", "demigod");
//
//        Vertex alcmene = graph.addVertex(null);
//        ElementHelper.setProperties(alcmene, "name", "alcmene", "age", 45, "type", "human");
//
//        Vertex pluto = graph.addVertex(null);
//        ElementHelper.setProperties(pluto, "name", "pluto", "age", 4000, "type", "god");
//
//        Vertex nemean = graph.addVertex(null);
//        ElementHelper.setProperties(nemean, "name", "nemean", "type", "monster");
//
//        Vertex hydra = graph.addVertex(null);
//        ElementHelper.setProperties(hydra, "name", "hydra", "type", "monster");
//
//        Vertex cerberus = graph.addVertex(null);
//        ElementHelper.setProperties(cerberus, "name", "cerberus", "type", "monster");
//
//        Vertex tartarus = graph.addVertex(null);
//        ElementHelper.setProperties(tartarus, "name", "tartarus", "type", "location");
//
//        // edges
//
//        jupiter.addEdge("father", saturn);
//        jupiter.addEdge("lives", sky).setProperty("reason", "loves fresh breezes");
//        jupiter.addEdge("brother", neptune);
//        jupiter.addEdge("brother", pluto);
//
//        neptune.addEdge("lives", sea).setProperty("reason", "loves waves");
//        neptune.addEdge("brother", jupiter);
//        neptune.addEdge("brother", pluto);
//
//        hercules.addEdge("father", jupiter);
//        hercules.addEdge("mother", alcmene);
//        ElementHelper.setProperties(hercules.addEdge("battled", nemean), "time", 1, "place", Geoshape.point(38.1f, 23.7f));
//        ElementHelper.setProperties(hercules.addEdge("battled", hydra), "time", 2, "place", Geoshape.point(37.7f, 23.9f));
//        ElementHelper.setProperties(hercules.addEdge("battled", cerberus), "time", 12, "place", Geoshape.point(39f, 22f));
//
//        pluto.addEdge("brother", jupiter);
//        pluto.addEdge("brother", neptune);
//        pluto.addEdge("lives", tartarus).setProperty("reason", "no fear of death");
//        pluto.addEdge("pet", cerberus);
//
//        cerberus.addEdge("lives", tartarus);
//
//        // commit the transaction to disk
//        graph.commit();
//    }
}
