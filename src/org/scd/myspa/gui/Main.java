package org.scd.myspa.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.stage.Stage;

/**
 *
 * @author zende
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/scd/myspa/gui/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");//Aquí agregamos la librería de bootstrap 
        primaryStage.getIcons().add(new Image("/org/scd/myspa/resources/logoSPA.png"));
        primaryStage.setTitle("MySPA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
