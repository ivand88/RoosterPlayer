/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import roosterplayer.player.media.Playlist;
import roosterplayer.player.media.PlaylistItem;
import roosterplayer.player.player.RoosterManager;

/**
 *
 * @author Ivan
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private MenuBar mainMenu;
    
    @FXML
    private Button stopButton, nextItemButton, playButton, roosterLibrary, roosterPlaylist, 
            roosterEqualizer, roosterSettings, roosterMisc;
    @FXML
    private AnchorPane actionPane, sideActionPane;

    
    @FXML
    private StackPane playerMainArrea;
    
    @FXML
    private RoosterManager player;

    @FXML
    private BorderPane borderPane;
        
    @FXML
    private Pane holder;
    
    @FXML
    private VBox sideActionPaneInner;
    
    @FXML
    private Slider progressBar, volumeControll, panStereoControll;
            
    ObservableList<PlaylistItem> playlistItems;
    ListView lv;
    DoubleProperty currentProgress;
    boolean fullscreen = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        player = new RoosterManager();
        lv = new ListView();
        
        // Move to fxml
        progressBar.setMin(0);
        progressBar.setMax(100);
         
        progressBar.setOnMouseReleased((e)->{
            player.seek(progressBar.getValue());
        });
        
        Playlist playlistOne = new Playlist();
        player.addPlaylistItem(playlistOne);
        
        // Re-write
        /* Update size for responsive layout */
        borderPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double actionPaneHeight = actionPane.getHeight();
            double sideActionPaneWidth = sideActionPane.getWidth();
            
            double borderPaneWidth = ((Stage) borderPane.getScene().getWindow()).getWidth();
            double borderPaneHeight = ((Stage) borderPane.getScene().getWindow()).getHeight();
            
            
            playerMainArrea.setMaxWidth(borderPaneWidth - sideActionPaneWidth);
            playerMainArrea.setMaxHeight(borderPaneHeight - actionPaneHeight);
        });
        
        /* Bind Slider Controlls */
        volumeControll.valueProperty().addListener((observable, oldValue, newValue) -> {
            player.setVolume((double) newValue);
        });
        
        panStereoControll.valueProperty().addListener((observable, oldValue, newValue) -> {
            player.setBalance((double) newValue);
        });
        
        /* Enter / Exit Full Screen Mode */
        playerMainArrea.setOnMouseClicked((MouseEvent e) -> {
            if (e.getClickCount() == 2){
                if (!fullscreen) handleFullScreenMode();
                else handleFullScreenModeExit();
            }
        });
        
            /* Stage move on drag event */
            final Delta dragDelta = new Delta();
            mainMenu.setOnMousePressed(new EventHandler<MouseEvent>() {
              @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = ((Stage) borderPane.getScene().getWindow()).getX() - mouseEvent.getScreenX();
                dragDelta.y = ((Stage) borderPane.getScene().getWindow()).getY() - mouseEvent.getScreenY();
              }
            });
            mainMenu.setOnMouseDragged(new EventHandler<MouseEvent>() {
              @Override public void handle(MouseEvent mouseEvent) {
                double width = ((Stage) borderPane.getScene().getWindow()).getWidth();
                double height = ((Stage) borderPane.getScene().getWindow()).getHeight();
                
                //if ( mouseEvent.getSceneX() > 10 && mouseEvent.getSceneY() > 10 && mouseEvent.getSceneX() < width - 10 && mouseEvent.getSceneY() < height - 10){
                //    System.out.println("YUP");
                    ((Stage) borderPane.getScene().getWindow()).setX(mouseEvent.getScreenX() + dragDelta.x);
                    ((Stage) borderPane.getScene().getWindow()).setY(mouseEvent.getScreenY() + dragDelta.y);
                /*} else {
                    System.out.println("NOPE");
                }*/
              }
            });

            // records relative x and y co-ordinates.
            class Delta { double x, y; } 

        
        updatePlaylist();
    }
    
    /* FXML events */
    @FXML
    public void handleOpenMedia(){
        FileChooser chooser = new FileChooser();
        
        File selectedFile = chooser.showOpenDialog(new Stage());
        if (selectedFile != null){
            player.getCurrentPlaylist().addPlaylistItem(new PlaylistItem(selectedFile));

            initPlayer();
        }
    }
    
    @FXML
    public void handleOpenDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        
        File selectedDirectory = chooser.showDialog(new Stage());
        if (selectedDirectory != null){
            for (File selectedFile: selectedDirectory.listFiles()){
                player.getCurrentPlaylist().addPlaylistItem(new PlaylistItem(selectedFile));
            }
        
            initPlayer();
        }
    }
    
    
    private void initPlayer(){
        // After we have at least one file, get mediaView
        playerMainArrea.getChildren().clear();
        
        holder = player.initMediaPlayer();
                
        currentProgress = player.getCurrentProgress();
        
        currentProgress.addListener((observable, oldValue, newValue) -> {
            if (!progressBar.isPressed())
                progressBar.setValue((double) newValue);
            
        });
        
         //player.setVolume(player.getVolume());
         //volumeControll.setValue(player.getVolume());
        
        // Do not remove this otherwise video won't be shown
        playerMainArrea.getChildren().add(holder.getChildren().get(0));
    }
    
    @FXML 
    public void handlePlayAction(){
        player.play();
    }
    
    public void handlePlayItemAction(PlaylistItem playlistItem){
        player.stop();
        player.playItem(playlistItem);
        
        initPlayer();
        player.play();
    }
    
    @FXML 
    public void handlePauseAction(){        
        player.pause();
    }
    
    @FXML 
    public void handleStopAction(){
        player.stop();
    }
    @FXML 
    public void handleNextAction(){
        player.stop();
        player.getCurrentPlaylist().getNextPlaylistItem();

        initPlayer();
        //player.initMediaPlayer(); // MOVE
        player.play();
    }  
    
    @FXML 
    public void handlePreviousAction(){
        player.stop();
        player.getCurrentPlaylist().getPreviousPlaylistItem();
        
        initPlayer();
        //player.initMediaPlayer(); // MOVE
        player.play();    
    }    
    
    @FXML
    public void handleOpenPlaylist(){
        
    }
    
    @FXML
    public void handleSavePlaylist(){
        for (PlaylistItem item: player.getCurrentPlaylist().getPlaylistItems()){
            System.out.println(item.getFileName());
        }
       // setSize();
    }
    
    @FXML
    public void handleExitApp(){
        player.dispose();
        System.exit(0);
    }
    
    private void updatePlaylist(){
        playlistItems = player.getCurrentPlaylist().getPlaylistItems();
        
        lv.setItems(playlistItems);
        
        lv.setOnKeyReleased((KeyEvent event) -> {
            player.getCurrentPlaylist().removePlaylistItem((PlaylistItem) lv.getSelectionModel().getSelectedItem());
            System.out.println("remove " + ((PlaylistItem) lv.getSelectionModel().getSelectedItem()).getFileName());
        });
        
        playlistItems.addListener( new ListChangeListener(){
            @Override
            public void onChanged(ListChangeListener.Change c) {
                lv.setCellFactory(new Callback<ListView<PlaylistItem>, ListCell<PlaylistItem>>() {
                    @Override
                    public ListCell<PlaylistItem> call(ListView<PlaylistItem> playlistItemsView) {
                        PlaylistItemCell pic = new PlaylistItemCell();
                                
                        pic.setOnMouseClicked((MouseEvent evt)->{
                            if (evt.getClickCount() == 2){
                                handlePlayItemAction(pic.getItem());
                            }
                        });
                        
                        return pic;
                    }
                });
            }
            
        });

        lv.setPrefWidth(160);
        lv.autosize();
        
        //sideActionPane.getChildren().remove(lv);
        lv.setManaged(true);
        sideActionPaneInner.getChildren().add( lv );
    }
    
    
    /* TODO'S */
    /* TODO'S */
    /* TODO'S */
    @FXML 
    public void handleUpdates(){
        // TODO
    }
    
    @FXML 
    public void handleSettings(){
        // TODO
    }
    
    @FXML 
    public void handleHelpScreen(){
        // TODO
    }
    @FXML 
    public void handleAboutScreen(){
        // TODO
    }

    @FXML 
    public void handleFullScreenMode(){
        hideSideActionPane();
        ((Stage) borderPane.getScene().getWindow()).setFullScreen(true);
        
        this.fullscreen = true;
    }
    
    @FXML 
    public void handleFullScreenModeExit(){
        ((Stage) borderPane.getScene().getWindow()).setFullScreen(false);
        
        this.fullscreen = false;
    }
    
    public void hideSideActionPane(){
        borderPane.setRight(null);
    }
    
    public void showSideActionPane(){
        borderPane.setRight(sideActionPane);
    }
    
    class Delta { double x, y; }  
    
}
