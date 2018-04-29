/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer.player.media;

import java.io.File;

/**
 *
 * @author Ivan
 */
public class PlaylistItem {
    
    private String fileName, filePath, fileExtension, type;
    private File file;

    /**
     * @param file the file to set
     */
    public PlaylistItem(File file){
        this.file = file;
        
        setFilePath(file.getAbsolutePath());
        setFileName(file.getName());
        setFileExtension();
    }
    
    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return the extension
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * @param
     */
    public void setFileExtension() {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            this.fileExtension = fileName.substring(i+1);
        }
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @param fileName the fileName to set
     */
    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @return the fileName
     */
    public String getFileName() {
        return this.fileName;
    }
}
