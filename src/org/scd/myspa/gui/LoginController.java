/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scd.myspa.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

/**
 * FXML Controller class
 *
 * @author zende
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField txtUsuario;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnIngresar;

    @FXML
    private JFXButton btnSalir;

    // Generamos la instancia del objeto Client de javax.ws
    Client client = ClientBuilder.newClient();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

  
      public void validarUsuario() {
        String user = txtUsuario.getText();
        String pass = txtPassword.getText();
        if ("".equals(user) && "".equals(pass)) {
            
            Alert alert = new Alert(Alert.AlertType.WARNING, "Debe ingresar un usuario y contraseña.");
            alert.setHeaderText("¡¡¡ Alerta !!!");
            alert.show();
        } else {
            try {
                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
                Response response = client.target("http://localhost:8080/myspa/api/login/validate")
                        .queryParam("nombreUsuario", user)
                        .queryParam("contrasenia", pass)
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.form(map));
                String resp = response.readEntity(String.class);
                Gson g = new Gson();
                JsonObject jso = g.fromJson(resp, JsonObject.class);
                if (jso.get("error") != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText(jso.get("error").getAsString());
                    alert.showAndWait();
                    return;
                }

                Parent principal = FXMLLoader.load(getClass().getResource("/org/scd/myspa/gui/fxml/Principal.fxml"));
                Scene scene = new Scene(principal);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.getIcons().add(new Image("/org/scd/myspa/resources/logoSPA.png"));
                primaryStage.setTitle("Sistema de Control MySPA");
                primaryStage.show();

                Stage cerraVentana = (Stage) btnSalir.getScene().getWindow();
                cerraVentana.close();

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar sistema");
                alert.show();
                ex.printStackTrace();
            }
        }
    }
    public void salir() {
        Stage cerraVentana = (Stage) btnSalir.getScene().getWindow();
        cerraVentana.close();
    }
}
