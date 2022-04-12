/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scd.myspa.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.scd.myspa.core.model.Horario;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
public class HorarioController implements Initializable {

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXButton btnModificar;

    @FXML
    private TableView<Horario> tblHorario;
    @FXML
    private TableColumn<Horario, ?> colCodigo;

    @FXML
    private TableColumn<Horario, ?> colHoraIn;

    @FXML
    private TableColumn<Horario, ?> colHoraFin;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnNuevo;

    @FXML
    private JFXTextField txtCodigo;

    @FXML
    private JFXTextField txtHoraInicio;

    @FXML
    private JFXTextField txtHoraFin;

    @FXML
    private JFXTextField txtFiltro;

    ObservableList<Horario> horarioList;
     private ObservableList<Horario> filtroHorario;

    // Generamos la instancia del objeto Client de javax.ws
    Client client = ClientBuilder.newClient();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mapeado de columnas
        this.colCodigo.setCellValueFactory(new PropertyValueFactory("id"));
        this.colHoraIn.setCellValueFactory(new PropertyValueFactory("horaInicio"));
        this.colHoraFin.setCellValueFactory(new PropertyValueFactory("horaFin"));

        horarioList = FXCollections.observableArrayList();
        filtroHorario = FXCollections.observableArrayList();
        cargarTablaHorario();

    }

    public void saveHorario() {
        Horario h = new Horario();

        h.setId(0);
        h.setHoraInicio(txtHoraInicio.getText());
        h.setHoraFin(txtHoraFin.getText());

        if (h == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se ingreso ningún dato para guardar.");
            alert.showAndWait();
        }

        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            Response response = client.target("http://localhost:8080/myspa/api/horario/save")
                    .queryParam("idHorario", h.getId())
                    .queryParam("horaInicio", h.getHoraInicio())
                    .queryParam("horaFin", h.getHoraFin())
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Horario Agregado");
            alert.setContentText("El horario se agregó corectamente.");
            alert.showAndWait();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.tblHorario.refresh();
        cargarTablaHorario();
        limpiarFormulario();
    }

    public void updateHorario() {
        Horario h = this.tblHorario.getSelectionModel().getSelectedItem();
        if (h == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se selecciono ningún horario para actualizar.");
            alert.showAndWait();
        }

        Horario update = new Horario();

        update.setHoraInicio(txtHoraInicio.getText());
        update.setHoraFin(txtHoraFin.getText());

        if (!this.horarioList.contains(update)) {
            h.setHoraInicio(update.getHoraInicio());
            h.setHoraFin(update.getHoraFin());
        }
        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            Response response = client.target("http://localhost:8080/myspa/api/horario/save")
                    .queryParam("idHorario", h.getId())
                    .queryParam("horaInicio", h.getHoraInicio())
                    .queryParam("horaFin", h.getHoraFin())
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Horario Actualizado");
            alert.setContentText("El horario se actualizó corectamente.");
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.tblHorario.refresh();
        cargarTablaHorario();
        limpiarFormulario();

    }

    public void deleteHorario() {

        Horario h = this.tblHorario.getSelectionModel().getSelectedItem();
        if (h == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se selecciono ningún horario para eliminar.");
            alert.showAndWait();
        }
        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            Response response = client.target("http://localhost:8080/myspa/api/horario/delete")
                    .queryParam("idHorario", h.getId())
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Horario Eliminado");
            alert.setContentText("El horario se elimino correctamente.");
            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.tblHorario.refresh();
        cargarTablaHorario();
        limpiarFormulario();
    }

    public void cargarTablaHorario() {
        try {
            String response = client.target("http://localhost:8080/myspa/api/horario/getAll")
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);

            Gson gson = new Gson();

            ArrayList<Horario> lista = gson.fromJson(response, new TypeToken<List<Horario>>() {
            }.getType());

            horarioList = FXCollections.observableArrayList(lista);
            tblHorario.setItems(horarioList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarDetalleHorario() {
        Horario h = this.tblHorario.getSelectionModel().getSelectedItem();

        if (h != null) {
            this.txtCodigo.setText(String.valueOf(h.getId()));
            this.txtHoraInicio.setText(h.getHoraInicio());
            this.txtHoraFin.setText(h.getHoraFin());
        }

    }

    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
    }
    
     @FXML
    private void txtFiltro(KeyEvent event) {
 
       String filtro = this.txtFiltro.getText();
       int f = Integer.parseInt(this.txtFiltro.getText());
       
       if(filtro.isEmpty()){
           this.tblHorario.setItems(horarioList);
       }
       else{
           //Limpiamos la lista
           this.filtroHorario.clear();
           
           for(Horario h : this.horarioList){
               if(h.getHoraInicio().contains(filtro) || h.getHoraFin().contains(filtro)){
                   this.filtroHorario.add(h);
               }
           }
           
           this.tblHorario.setItems(filtroHorario);
       }

    }
    


}
