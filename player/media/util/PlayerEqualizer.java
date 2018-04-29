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
public class PlayerEqualizer {
    private static PlayerEqualizer playerEqualizer;
    
    private PlayerEqualizer(){
        
    }
    
    public static PlayerEqualizer getInstance(){
        
        if (playerEqualizer == null){
            synchronized(PlayerSettings.class){
                if (playerEqualizer == null){
                    playerEqualizer = new PlayerEqualizer();
                }
            }
        }
        
        return playerEqualizer;
    }
    
    
}
