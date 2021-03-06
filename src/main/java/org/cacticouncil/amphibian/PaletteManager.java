package org.cacticouncil.amphibian;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

/**
 * Created by exlted on 26-Apr-17.
 */
public class PaletteManager
{

    private String basePaletteList = "CoffeeScript|coffeescript\\n" +
            "JavaScript|javascript\\n" +
            "Python|python\\n" +
            "Java|java";

    private String paletteList = basePaletteList;
    private StringBuilder forNewList;
    public String paletteDirectory;

    private static PaletteManager paletteManager = null;

    private PaletteManager(){

    }

    private void traverse(File dir){
        if(dir.isDirectory()){
            String[] children = dir.list();
            for (int i = 0; children != null && i < children.length; i++) {
                traverse(new File(dir, children[i]));
            }
        }
        if(dir.isFile()){
            if(dir.getName().endsWith(".json")){
                forNewList.append("\\n");
                forNewList.append(dir.getName().replaceFirst(".json", ""));
                forNewList.append('|');
                forNewList.append("USR");
                forNewList.append(dir.getName());
            }
        }
    }

    void updatePaletteList(Project proj){
        String newPaletteList = basePaletteList;
        forNewList = new StringBuilder("");
        VirtualFile vFile = ProjectRootManager.getInstance(proj).getContentSourceRoots()[0];
        vFile = vFile.getParent();
        String fileLoc = vFile.toString();
        fileLoc = fileLoc.replaceFirst("file://", "");
        fileLoc = fileLoc + System.getProperty("file.separator") + "palettes";
        File toTraverse = new File(fileLoc);
        toTraverse.mkdirs();
        traverse(toTraverse);
        paletteDirectory = toTraverse.getAbsolutePath() + System.getProperty("file.separator");


        //Deal with filling out the new list here
        //for each fileName in userPaletteFolder{
        //  if(fileName.endsWith('.coffee') then
        //      String fileStart = fileName.split('.')[0];
        //      newPaletteList.append('\\n');
        //      newPaletteList.append(fileStart);
        //      newPaletteList.append('|');
        //      newPaletteList.append(USR);
        //      newPaletteList.append(fileName);
        paletteList = newPaletteList + forNewList.toString();
    }

    String getPaletteList(){
        return paletteList;
    }

    public static PaletteManager getPaletteManager(){
        if(paletteManager == null){
            paletteManager = new PaletteManager();
        }
        return paletteManager;
    }

}