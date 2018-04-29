/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer.player.player;

import java.io.File;
import roosterplayer.player.media.Playlist;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import roosterplayer.player.media.PlaylistItem;
import roosterplayer.player.media.util.PlayerEqualizer;
import roosterplayer.player.media.util.PlayerSettings;

/**
 *
 * @author Ivan
 */
public class RoosterManager {
    
    private ArrayList<Playlist> playlist;
    private Playlist currentPlaylist = null;
    
    PlayerSettings settings;
    PlayerEqualizer equalizer;
    private RoosterPlayer mediaPlayer;
    private double volume;
    private double balance;
    
    public RoosterManager(){
        // on init
        playlist = new ArrayList();
        
        // Read from settings-history
        this.volume = 80;
        this.balance = 0;
    }
    
    /**
     * @param item the PlaylistItem to add
     */
    public void addPlaylistItem(Playlist item){
        playlist.add(item);
    }
    
    /**
     * @param item the PlaylistItem to remove
     */
    public void removePlaylistItem(Playlist item){
        playlist.remove(item);
    }
    
    /**
     * @return playlistItems size
     */
    public int countPlaylists(){
        return playlist.size();
    }
    
    public Playlist getCurrentPlaylist(){
        if (currentPlaylist == null ){
            currentPlaylist = playlist.get(0);
        }
        
        return currentPlaylist;
    }
    
    public void nextPlaylist(){
        int currentPlaylistIndex = playlist.indexOf(currentPlaylist);
        int nextPlaylistIndex = currentPlaylistIndex + 1;
        
        if (nextPlaylistIndex < countPlaylists()){
            currentPlaylist = playlist.get(nextPlaylistIndex);
        }
    }
    
    public void previousPlaylist(){
        int currentPlaylistIndex = playlist.indexOf(currentPlaylist);
        int previousPlaylistIndex = currentPlaylistIndex - 1;
        
        if (previousPlaylistIndex <= countPlaylists() && previousPlaylistIndex >= 0){
            currentPlaylist = playlist.get(previousPlaylistIndex);
        }
        
    }
    
    
    
    public Pane initMediaPlayer(){
        
            File currentFile = currentPlaylist.getCurrentItem().getFile();
            if (mediaPlayer != null) mediaPlayer.dispose();
            
            mediaPlayer = new RoosterJFXMediaPlayer();
            mediaPlayer.load(currentFile);

            Pane holder = new Pane();
            holder.getChildren().add(mediaPlayer.getView());

            mediaPlayer.setVolume(volume);
            mediaPlayer.setBalance(balance);
            
        return holder;
    }
    
    public double getTotalDuration(){
        System.out.println(mediaPlayer.getTotalDuration());
        return mediaPlayer.getTotalDuration();
    }
    
    public DoubleProperty getCurrentProgress(){
        return mediaPlayer.getCurrentProgress();
    }
    
    
    
    public void play(){
        mediaPlayer.play();
    }
    
    public void pause(){
        mediaPlayer.pause();
    }
    
    public void stop(){
        mediaPlayer.stop();
    }
    
    public void dispose(){
        if (mediaPlayer != null) mediaPlayer.dispose();
    }
    

    
    public void playItem(PlaylistItem playlistItem) {
        currentPlaylist.setCurrentItem(playlistItem);
    }
    
    public void seek(double time){
        mediaPlayer.seek(time);
    }
    
    public void setVolume(double vol){
        this.volume = vol;
        
        mediaPlayer.setVolume(vol);
    }
    
    public double getVolume(){        
        return volume;
    }

    public void setBalance(double d) {
        this.balance = d;
        mediaPlayer.setBalance(d);
    }
    
    public double getBalance(){
        return this.balance;
    }
    
}
