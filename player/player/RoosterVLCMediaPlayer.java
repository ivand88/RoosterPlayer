/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer.player.player;

import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import java.io.File;
import java.nio.ByteBuffer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import javafx.beans.property.SimpleDoubleProperty;
import uk.co.caprica.vlcj.binding.internal.libvlc_audio_output_channel_t;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 *
 * @author Ivan
 */
public class RoosterVLCMediaPlayer extends RoosterPlayer {

    private File file;
    
    private String PATH_TO_VIDEO;

    private ImageView imageView;

    private DirectMediaPlayerComponent mediaPlayerComponent;

    private WritableImage writableImage;

    private Pane playerHolder;

    private WritablePixelFormat<ByteBuffer> pixelFormat;

    private FloatProperty videoSourceRatioProperty;
    
    private boolean STARTED = false;
    
    private DoubleProperty currentProgress = new SimpleDoubleProperty(0);
    
    private double balance;
    
    @Override
    void load(File file) {
        if (mediaPlayerComponent == null) init();
        this.file = file;
        this.PATH_TO_VIDEO = file.getAbsolutePath();
    }

    @Override
    void play() {
        if (!STARTED){
            mediaPlayerComponent.getMediaPlayer().prepareMedia(PATH_TO_VIDEO);
            mediaPlayerComponent.getMediaPlayer().start();
        } else {
            mediaPlayerComponent.getMediaPlayer().pause();
        }
    }

    @Override
    void stop() {
        mediaPlayerComponent.getMediaPlayer().unlock();
        mediaPlayerComponent.getMediaPlayer().stop();
        STARTED = false;
    }

    @Override
    void pause() {
        mediaPlayerComponent.getMediaPlayer().pause();
    }

    @Override
    void init() {
        NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC\\");
        
        mediaPlayerComponent = new CanvasPlayerComponent();
        playerHolder = new Pane();
        videoSourceRatioProperty = new SimpleFloatProperty(0.4f);
        pixelFormat = PixelFormat.getByteBgraPreInstance();
                
        mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener( new MediaPlayerEventAdapter(){

            @Override
            public void positionChanged(uk.co.caprica.vlcj.player.MediaPlayer mediaPlayer, float newPosition) {
                currentProgress.setValue(newPosition * 100);
            }

        });
        
        initializeImageView();

    }
    
    @Override
    Pane getView() {
        return playerHolder;
    }

    @Override
    void dispose() {
        mediaPlayerComponent.getMediaPlayer().release();
    }

    @Override
    double getVolume() {
        // int
        return mediaPlayerComponent.getMediaPlayer().getVolume();
    }

    @Override
    void setVolume(double volume) {
        // int
        int v = (int) volume;
        mediaPlayerComponent.getMediaPlayer().setVolume(v);
    }
    
    private void initializeImageView() {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        writableImage = new WritableImage((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
        imageView = new ImageView(writableImage);
        playerHolder.getChildren().add(imageView);

        playerHolder.widthProperty().addListener((observable, oldValue, newValue) -> {
            fitImageViewSize(newValue.floatValue(), (float) playerHolder.getHeight());
        });
        
        playerHolder.heightProperty().addListener((observable, oldValue, newValue) -> {
            fitImageViewSize((float) playerHolder.getWidth(), newValue.floatValue());
        });

        videoSourceRatioProperty.addListener((observable, oldValue, newValue) -> {
            fitImageViewSize((float) playerHolder.getWidth(), (float) playerHolder.getHeight());
        });
        
    }

    
    private void fitImageViewSize(float width, float height) {
        Platform.runLater(() -> {
            float fitHeight = videoSourceRatioProperty.get() * width;
            if (fitHeight > height) {
                imageView.setFitHeight(height);
                double fitWidth = height / videoSourceRatioProperty.get();
                imageView.setFitWidth(fitWidth);
                imageView.setX((width - fitWidth) / 2);
                imageView.setY(0);
            }
            else {
                imageView.setFitWidth(width);
                imageView.setFitHeight(fitHeight);
                imageView.setY((height - fitHeight) / 2);
                imageView.setX(0);
            }
        });
    }

    @Override
    double getTotalDuration() {
        return mediaPlayerComponent.getMediaPlayer().getLength();
    }

    @Override
    DoubleProperty getCurrentProgress() {
        return currentProgress;
    }

    @Override
    void seek(double time) {
        mediaPlayerComponent.getMediaPlayer().setPosition((float) time / 100);
    }

    @Override
    void setBalance(double balance) {
        if (this.balance != (int) balance){
            balance = (int) balance;

                libvlc_audio_output_channel_t channel = libvlc_audio_output_channel_t.libvlc_AudioChannel_Stereo;

                if (balance > 0){
                    channel = libvlc_audio_output_channel_t.libvlc_AudioChannel_Right;
                } else if (balance < 0){
                    channel = libvlc_audio_output_channel_t.libvlc_AudioChannel_Left;
                } else {
                    // Do nothing
                }

            mediaPlayerComponent.getMediaPlayer().setAudioChannel(channel.intValue());
        }
    }
    

    @Override
    double getBalance() {
        return this.balance;
    }
    
    private class CanvasPlayerComponent extends DirectMediaPlayerComponent {

        public CanvasPlayerComponent() {
            super(new CanvasBufferFormatCallback());
        }

        PixelWriter pixelWriter = null;

        private PixelWriter getPW() {
            if (pixelWriter == null) {
                pixelWriter = writableImage.getPixelWriter();
            }
            return pixelWriter;
        }

        @Override
        public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
            if (writableImage == null) {
                return;
            }
            Platform.runLater(() -> {
                Memory nativeBuffer = mediaPlayer.lock()[0];
                try {
                    ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
                    getPW().setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
                }
                finally {
                    mediaPlayer.unlock();
                }
            });
        }
    }

    private class CanvasBufferFormatCallback implements BufferFormatCallback {
        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            Platform.runLater(() -> videoSourceRatioProperty.set((float) sourceHeight / (float) sourceWidth));
            return new RV32BufferFormat((int) visualBounds.getWidth(), (int) visualBounds.getHeight());
        }
    }
    
}
