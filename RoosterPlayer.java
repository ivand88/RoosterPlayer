/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roosterplayer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import roosterplayer.util.ResizeHelper;

/**
 *
 * @author Ivan
 */
public class RoosterPlayer extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        String css = this.getClass().getResource("assets/rooster_player.css").toExternalForm();
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);

        stage.initStyle(StageStyle.UNDECORATED);
        
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        
        ResizeHelper.addResizeListener(stage);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
