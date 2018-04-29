/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import roosterplayer.player.media.PlaylistItem;

/**
 *
 * @author Ivan
 */
class PlaylistItemCell extends ListCell<PlaylistItem>{
    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private GridPane gridPane;
    private FXMLLoader mLLoader = null;
    
    public PlaylistItemCell() {
        // PlaylistItemCell constructed
    }
    
    protected void updateItem(PlaylistItem playlistItem, boolean empty) {
        super.updateItem(playlistItem, empty);

        if(empty || playlistItem == null) {

            setText(null);
            setGraphic(null);

        } else {

            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("ListCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            label1.setText(String.valueOf(playlistItem.getFileName()));
            label2.setText(String.valueOf(playlistItem.getFileExtension()));


            setText(null);
            setGraphic(gridPane);
        }

    }
}
