/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer.player.media;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 *
 * @author Ivan
 */
public class Playlist {
    
    private String name;
    private ObservableList<PlaylistItem> playlistItems;
    private PlaylistItem currentItem = null, previousItem = null;

    // to private
    ListView<PlaylistItem> list;
            
    int playlistSize = 0;
    
    public Playlist(){
        // on init
        playlistItems = FXCollections.observableArrayList();
    }
    
    public void rewindPlaylist(){
        currentItem = getPlaylistItems().get(0);
    }
    
    public PlaylistItem getNextPlaylistItem(){
        if (getCurrentItem() == null){
            currentItem = getPlaylistItems().get(0);
        } else {
            previousItem = getCurrentItem();
            
            int index = getPlaylistItems().indexOf(getCurrentItem());
            int nextItem = index + 1;
            
            if (playlistSize > nextItem){
                currentItem = getPlaylistItems().get(nextItem);
            }
        }
        
        return getCurrentItem();
    }
    
    public PlaylistItem getPreviousPlaylistItem(){
        if (previousItem == null && getCurrentItem() == null){
            currentItem = getPlaylistItems().get(0);
            previousItem = getCurrentItem();
        } else if (previousItem == null){
            previousItem = getCurrentItem();
        } else {
            int index = getPlaylistItems().indexOf(getCurrentItem());
            
            // temporary fix
            int currentItemIndex = index - 1;
            int previousItemIndex = currentItemIndex - 1;
            
            if (currentItemIndex > 0){
                currentItem = getPlaylistItems().get(currentItemIndex);
                previousItem = getPlaylistItems().get(previousItemIndex); 
            } else if ( currentItemIndex == 0) {
                currentItem = getPlaylistItems().get(currentItemIndex);
                previousItem = getPlaylistItems().get(currentItemIndex);
            }
            // temporary fix end
        }
        
        return getCurrentItem();
    }
    
    /**
     * @param item the PlaylistItem to add
     */
    public void addPlaylistItem(PlaylistItem item){
        playlistSize++;
        getPlaylistItems().add(item);
    }
    
    /**
     * @param item the PlaylistItem to remove
     */
    public void removePlaylistItem(PlaylistItem item){
        playlistSize--;
        getPlaylistItems().remove(item);
    }
    
    /**
     * @return playlistItems size
     */
    public int countPlaylistItems(){
        return getPlaylistItems().size();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the playlistItems
     */
    public ObservableList<PlaylistItem> getPlaylistItems() {
        return playlistItems;
    }

    /**
     * @return the currentItem
     */
    public PlaylistItem getCurrentItem() {
        if (currentItem == null) currentItem = playlistItems.get(0);
        
        return currentItem;
    }

    public void setCurrentItem(PlaylistItem playlistItem) {
        if (currentItem != null){
            previousItem = currentItem;
        } else {
            previousItem = playlistItem;
        }
        
        currentItem = playlistItem;
    }
    
}
