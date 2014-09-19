/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codemap1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mdunsdon
 */
public class FileUtils {
    
    private List<String> fileList;

    public FileUtils() {
        this.fileList = new ArrayList<>();
    }
    
    public void getAllFiles(File f){
        if(f.isFile()){
            try {
                appendToFileList(f.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(f.isDirectory()){
            for (File f2 : f.listFiles() ) {
                getAllFiles(f2);
            }
        }
        
    }

    /**
     * @return the fileList
     */
    public List<String> getFileList() {
        return fileList;
    }

    public void appendToFileList(String s) {
        this.fileList.add(s);
    }
    
}
