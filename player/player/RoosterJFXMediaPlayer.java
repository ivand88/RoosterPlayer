/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer.player.player;

import java.io.File;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 *
 * @author Ivan
 */
final public class RoosterJFXMediaPlayer extends RoosterPlayer {

    MediaPlayer player;
    MediaView playerView;
    Pane pane;
    
    private DoubleProperty currentProgress = new SimpleDoubleProperty(0);
    
    @Override
    void load(File file) {
        this.file = file;
        
        init();
    }

    @Override
    void play() {
        player.play();
    }

    @Override
    void stop() {
        player.stop();
    }

    @Override
    void pause() {
        player.pause();
    }

    @Override
    void init() {
        Media media = new Media(file.toURI().toString());
        player = new MediaPlayer(media);
        
    }

    @Override
    Pane getView() {
        pane = new Pane();
        playerView = new MediaView(player);
        setMediaEventHandlers(playerView);
        
        DoubleProperty mvw = playerView.fitWidthProperty();
        DoubleProperty mvh = playerView.fitHeightProperty();
        mvw.bind(pane.widthProperty());
        mvh.bind(pane.heightProperty());
        playerView.setPreserveRatio(true);
        
        pane.getChildren().add(playerView);

            pane.layoutBoundsProperty().addListener(new ChangeListener(){
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    updateSize((Bounds) newValue);
                }
            });
            
        
        return pane;
    }

    @Override
    void dispose() {
        player.dispose();
    }

    @Override
    double getVolume() {
        return player.getVolume();
    }

    @Override
    void setVolume(double volume) {
        player.setVolume(volume / 100);
    }
    
    
    /* PRIVATE */
    /* Media handlers */
    private void setMediaEventHandlers(final MediaView view) {
        final MediaPlayer player = view.getMediaPlayer();

        System.out.println("Initial: " + player.getStatus());
        
        player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                
                double val = (player.getCurrentTime().toMillis() / (player.getTotalDuration().toMillis() / 100));
                currentProgress.setValue(val);
            }
        });
        
        player.statusProperty().addListener((ObservableValue<? extends MediaPlayer.Status> observable, MediaPlayer.Status oldStatus, MediaPlayer.Status curStatus) -> {

            if (curStatus == MediaPlayer.Status.PLAYING){
                //
            }
            
            updateSize(pane.getLayoutBounds());
        });

        if (player.getError() != null) {
            System.out.println("Initial Error: " + player.getError());
        }

        player.setOnError(new Runnable() {
          @Override public void run() {
            System.out.println("Current Error: " + player.getError());
          }
        });
    }
    
    private void updateSize(Bounds newValue){
        Bounds xyPane = (Bounds) newValue;
                    
        double tempPaneX = xyPane.getWidth();
        double tempPaneY = xyPane.getHeight();
                    
        Bounds xyView = (Bounds) playerView.getLayoutBounds();
                    
        double tempViewX = xyView.getWidth();
        double tempViewY = xyView.getHeight();
                                                
        playerView.setX( ( tempPaneX - tempViewX ) / 2 );
        playerView.setY( ( tempPaneY - tempViewY ) / 2 );
    }

    @Override
    double getTotalDuration() {
        return player.getTotalDuration().toMillis();
    }

    @Override
    DoubleProperty getCurrentProgress() {
        return currentProgress;
    };

    @Override
    void seek(double time) {
        long value = (long) ((time * ( player.getTotalDuration().toMillis() / 100)));
        Duration duration = new Duration(value);
        player.seek(duration);
    }

    @Override
    void setBalance(double balance) {
        player.setBalance(balance);
    }

    @Override
    double getBalance() {
        return player.getBalance();
    }
    
}
