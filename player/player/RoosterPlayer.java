/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer.player.player;

import java.io.File;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;

/**
 *
 * @author Ivan
 */
abstract public class RoosterPlayer {
    
    private String filePath, fileName;
    File file;
    
    abstract void init();

    abstract void load(File file);
    
    abstract void play();
    
    abstract void pause();
    
    abstract void stop();
    
    abstract Pane getView();
    
    abstract void dispose();
    
    abstract double getVolume();
    
    abstract void setVolume(double volume);

    abstract double getTotalDuration();
    
    abstract DoubleProperty getCurrentProgress();

    abstract void seek(double time);

    abstract void setBalance(double balance);
    
    abstract double getBalance();
}
