/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codemap1;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGValueLink;

/**
 *
 * @author mdunsdon
 */
public class CodeMap1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileUtils fu = new FileUtils();
	String basePath = "C:\\WorkingCopy\\R4.3-6.6.1.0\\SF Gateway\\Web\\XIF\\App_Code";
        fu.getAllFiles(new File(basePath));
        //fu.getAllFiles(new File());
//        for(String fname : fu.getFileList()){
//            System.out.println(fname);
//        }
      
	//Add to titandb
	//tinkerTest tc = new tinkerTest();
	hgClass hg = new hgClass("/mike/HyperGraphDB/XIF");

	// Add files
        for(String projectPath : fu.getFileList()){
		projectPath  = projectPath.replace(basePath + "\\", "");
		String[] parts = projectPath.split(Pattern.quote("\\"));
		String fname = parts[parts.length - 1];
		//tc.addVertex("file", fname);
        }
	
	// Add methods
	Pattern methodUsePattern = Pattern.compile(".*[ .]([A-Za-z0-9_]*)\\(.*");
	Pattern methodDefPattern = Pattern.compile(".*(Function|Sub)[ ]+([A-Za-z0-9_]*)\\(.*");
        for(String fullPath : fu.getFileList()){
		String[] parts = fullPath.split(Pattern.quote("\\"));
		String fname = parts[parts.length - 1];
		String filePath  = fullPath.replace("\\" + fname, "");
		// find vertex for parent file	
	//	Vertex parentVertex = tc.getVertex(fname);
		
		vbFileClass vb = new vbFileClass();
		vb.setVertexLabel(fname);
		HGHandle fileVertex = hg.addVertex(vb);
		// Open file
		    File file = new File(fullPath);
		    FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
				Matcher m = methodDefPattern.matcher(line);
				// Found a method definition
				if(m.matches()){
					//Add method name to db	
					String methodName = m.group(2);
//					Vertex methodVertex = tc.addVertex("method", methodName);
					//Add edge linking this method to the file it's in
					//tc.addEdge(parentVertex, methodVertex, "contains");

					// Add the method object
					methodClass mc = new methodClass();
					mc.setVertexLabel(methodName);
					HGHandle methVertex = hg.addVertex(mc);
					// Link method to the file it's in
					HGValueLink link = new HGValueLink("contains", fileVertex, methVertex);
				}
			    }
			    br.close();
			    fr.close();
			} catch (FileNotFoundException ex) {
				Logger.getLogger(CodeMap1.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(CodeMap1.class.getName()).log(Level.SEVERE, null, ex);
			}
        }

	// Search for method calls and add the graph edges to document them
	String thisMethod="";
        for(String fullPath : fu.getFileList()){
		String[] parts = fullPath.split(Pattern.quote("\\"));
		String fname = parts[parts.length - 1];
		String filePath  = fullPath.replace("\\" + fname, "");
		// find vertex for parent file	
		//Vertex parentVertex = tc.getVertex(fname);
		HGHandle parentVertex = hg.findVertex(fname,vbFileClass.class);
		// Open file
		    File file = new File(fullPath);
		    FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
				Matcher matchDef = methodDefPattern.matcher(line);
				// Found a method definition
				if(matchDef.matches()){
					thisMethod = matchDef.group(2);
				}
				if(!thisMethod.isEmpty()){
					Matcher matchUse = methodUsePattern.matcher(line);
				
					// Found a method call
					if(matchUse.matches()){
						//find calling method vertex
						//GremlinPipeline pipe = new GremlinPipeline();
//						pipe.start(parentVertex).out("contains").property("name");
		//				GremlinPipeline pipe = new GremlinPipeline(parentVertex).out("contains").property("name").filter((String argument) -> argument.equals(thisMethod));						
						//find called method vertex
		//				Vertex calledMethodVertex = tc.getVertex(matchUse.group(1));
						HGHandle callingVertex = hg.findVertex(thisMethod,methodClass.class);
						HGHandle calledVertex = hg.findVertex(matchUse.group(1),methodClass.class);
						//Add edge linking this method to the file it's in
						HGValueLink link = new HGValueLink("calls", callingVertex, calledVertex);
					}
				}
			    }
			    br.close();
			    fr.close();
			} catch (FileNotFoundException ex) {
				Logger.getLogger(CodeMap1.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(CodeMap1.class.getName()).log(Level.SEVERE, null, ex);
			}
        }

	
//tc.dumpGraph();
	hg.dump();
	hg.closeGraph();
    }
   
}
