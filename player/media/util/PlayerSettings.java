/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer.player.media.util;

/**
 *
 * @author Ivan
 */
public class PlayerSettings {
    
    private static PlayerSettings playerSettings;
    
    private PlayerSettings(){
        
    }
    
    public static PlayerSettings getInstance(){
        if (playerSettings == null){
            synchronized(PlayerSettings.class){
                if (playerSettings == null){
                    playerSettings = new PlayerSettings();
                }
            }
        }
        
        return playerSettings;
    }
    
    public void getSettings(){
        
    }
    
    public void setSettings(){
        
    }
}
