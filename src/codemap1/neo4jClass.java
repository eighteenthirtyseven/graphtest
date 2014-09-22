package codemap1;

import java.io.File;
import java.util.Iterator;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.helpers.collection.IteratorUtil;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mdunsdon
 */
public class neo4jClass {
GraphDatabaseService graphDb;
Node firstNode;
Node secondNode;
Relationship relationship;	
ExecutionEngine engine;
private static final String DB_PATH = "target/XIF";
 public static enum RelTypes implements RelationshipType
    {
        CONTAINS,
        CALLS
    }

 public static enum nodeTypes{
		 File,
		 Method
 }

  neo4jClass()
    {
        neo4jClass n4j = new neo4jClass();
        n4j.createDb();
		// create an exectution engine in case we want to do any queries (which we undoubtably do)	
		engine = new ExecutionEngine( graphDb );
        //hello.removeData();
       // n4j.shutDown();
    }
 void createDb()
    {
        deleteFileOrDirectory( new File( DB_PATH ) );
        // START SNIPPET: startDb
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
		// Add an index for each node type so we can search
		for(nodeTypes nt: nodeTypes.values()){
				IndexDefinition indexDefinition;
				try ( Transaction tx = graphDb.beginTx() )
				{
					Schema schema = graphDb.schema();
					indexDefinition = schema.indexFor( DynamicLabel.label( nt.toString() ) )
							.on( "label" )
							.create();
					tx.success();
				}
		}
		
		
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb

//        // START SNIPPET: transaction
//        try ( Transaction tx = graphDb.beginTx() )
//        {
//            // Database operations go here
//            // END SNIPPET: transaction
//            // START SNIPPET: addData
//            firstNode = graphDb.createNode();
//            firstNode.setProperty( "message", "Hello, " );
//            secondNode = graphDb.createNode();
//            secondNode.setProperty( "message", "World!" );
//
//            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.CONTAINS );
//            relationship.setProperty( "message", "brave Neo4j " );
//            // END SNIPPET: addData
//
//            // START SNIPPET: readData
//            System.out.print( firstNode.getProperty( "message" ) );
//            System.out.print( relationship.getProperty( "message" ) );
//            System.out.print( secondNode.getProperty( "message" ) );
//            // END SNIPPET: readData
//
//            String greeting = ( (String) firstNode.getProperty( "message" ) )
//                       + ( (String) relationship.getProperty( "message" ) )
//                       + ( (String) secondNode.getProperty( "message" ) );
//
//            // START SNIPPET: transaction
//            tx.success();
//        }
        // END SNIPPET: transaction
    }

    void removeData()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.CALLS, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }

   public void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
		
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }

    // START SNIPPET: shutdownHook
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
    // END SNIPPET: shutdownHook

    private static void deleteFileOrDirectory( File file )
    {
        if ( file.exists() )
        {
            if ( file.isDirectory() )
            {
                for ( File child : file.listFiles() )
                {
                    deleteFileOrDirectory( child );
                }
            }
            file.delete();
        }
    } 

	public Node addVertex(String vertexType, String label) {
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode();
			node.setProperty(vertexType, label);
			tx.success();
			return node;
		}
	}
	
   	public void addEdge(Node nodeFrom, Node nodeTo, RelationshipType linkType, String linkProperty, String linkValue) {
		try (Transaction tx = graphDb.beginTx()) {
			Relationship rel = nodeFrom.createRelationshipTo(nodeTo, linkType);
			if(linkProperty != null){
				rel.setProperty(linkProperty, linkValue);
			}
			tx.success();
		}
	}

	public Node findVertex(String vertexType, String nodeLabel){
		Label nodeTypeLabel = DynamicLabel.label( vertexType );
		Node result=null;
//		graphDb.findNodesByLabelAndProperty( nodeTypeLabel, nodeLabel, this)

		ExecutionResult queryResult;
		try ( Transaction ignored = graphDb.beginTx() )
		{
			queryResult = engine.execute( "match (n) WHERE n.vertexType=" + nodeLabel + " return n" );
			Iterator<Node> n_column = queryResult.columnAs( "n" );
			for ( Node node : IteratorUtil.asIterable( n_column ) )
			{
				result = node;
			}
		}
		return result;
		
	}

	public void dump(){
			
		ExecutionResult queryResult;
		try ( Transaction ignored = graphDb.beginTx() )
		{
			queryResult = engine.execute( "match (f)-[CONTAINS]->(m) return n" );
			Iterator<Node> n_column = queryResult.columnAs( "n" );
			for ( Node node : IteratorUtil.asIterable( n_column ) )
			{
				
			}
		}
	}
	
}
