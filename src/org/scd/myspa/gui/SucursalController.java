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
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import org.scd.myspa.core.model.Sucursal;

/**
 * FXML Controller class
 *
 * @author zende
 */
public class SucursalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnNuevo;

    @FXML
    private JFXTextField txtIdSala;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtDomicilio;

    @FXML
    private JFXTextField txtLatitud;

    @FXML
    private JFXTextField txtLongitud;

    @FXML
    private JFXTextField txtEstatus;

    @FXML
    private TableView<Sucursal> tblSucursal;

    @FXML
    private TableColumn<Sucursal, ?> colId;

    @FXML
    private TableColumn<Sucursal, String> colNombre;

    @FXML
    private TableColumn<Sucursal, String> colDomicilio;

    @FXML
    private TableColumn<?, ?> colEstatus;

    @FXML
    private JFXTextField txtFiltro;

    @FXML
    private JFXButton btnModificar;

    ObservableList<Sucursal> sucursalList;
    private ObservableList<Sucursal> filtroSucursal;

    // Generamos la instancia del objeto Client de javax.ws
    Client client = ClientBuilder.newClient();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Mapeado de columnas
        this.colId.setCellValueFactory(new PropertyValueFactory("id"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colDomicilio.setCellValueFactory(new PropertyValueFactory("domicilio"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));

        sucursalList = FXCollections.observableArrayList();
        filtroSucursal = FXCollections.observableArrayList();
        cargarTablaSucursal();

    }

    public void saveSucursal() {

        Sucursal cnt = this.tblSucursal.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se encontraron datos para agregar la Sucursal.");
            alert.showAndWait();

        }
        Sucursal s = new Sucursal();

        s.setId(0);
        s.setNombre(txtNombre.getText());
        s.setDomicilio(txtDomicilio.getText());
        s.setLatitud(Double.parseDouble(txtLatitud.getText()));
        s.setLongitud(Double.parseDouble(txtLongitud.getText()));
        s.setEstatus(1);

        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            Response response = client.target("http://localhost:8080/myspa/api/sucursal/save")
                    .queryParam("idSucursal", s.getId())
                    .queryParam("nombre", s.getNombre())
                    .queryParam("domicilio", s.getDomicilio())
                    .queryParam("latitud", s.getLatitud())
                    .queryParam("longitud", s.getLongitud())
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Sucursal Agregada");
            alert.setContentText("La Sucursal se agregó corectamente.");
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cargarTablaSucursal();
        limpiarFrmulario();
    }

    public void updateSucursal() {
        Sucursal cnt = this.tblSucursal.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se encontraron datos para actualizar la Sucursal.");
            alert.showAndWait();

        }
        Sucursal suc = this.tblSucursal.getSelectionModel().getSelectedItem();
        Sucursal update = new Sucursal();

        update.setNombre(txtNombre.getText());
        update.setDomicilio(txtDomicilio.getText());
        update.setLatitud(Double.parseDouble(txtLatitud.getText()));
        update.setLongitud(Double.parseDouble(txtLongitud.getText()));

        if (!this.sucursalList.contains(update)) {
            suc.setNombre(update.getNombre());
            suc.setDomicilio(update.getDomicilio());
            suc.setLatitud(update.getLatitud());
            suc.setLongitud(update.getLongitud());

        }
        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            Response response = client.target("http://localhost:8080/myspa/api/sucursal/save")
                    .queryParam("idSucursal", suc.getId())
                    .queryParam("nombre", suc.getNombre())
                    .queryParam("domicilio", suc.getDomicilio())
                    .queryParam("latitud", suc.getLatitud())
                    .queryParam("longitud", suc.getLongitud())
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Sucursal Actualizada");
            alert.setContentText("La Sucursal se actualizó corectamente.");
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.tblSucursal.refresh();
        cargarTablaSucursal();
        limpiarFrmulario();
    }

    public void deleteSucursal() {
        Sucursal cnt = this.tblSucursal.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se encontraron datos para eliminar la Sucursal.");
            alert.showAndWait();

        }
        Sucursal suc = this.tblSucursal.getSelectionModel().getSelectedItem();

        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            Response response = client.target("http://localhost:8080/myspa/api/sucursal/delete")
                    .queryParam("idSucursal", suc.getId())
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Sucursal Eliminada");
            alert.setContentText("La Sucursal se eliminó corectamente.");
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.tblSucursal.refresh();
        cargarTablaSucursal();
        limpiarFrmulario();

    }

    public void cargarTablaSucursal() {
        try {
            String response = client.target("http://localhost:8080/myspa/api/sucursal/getAll")
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);

            Gson gson = new Gson();

            ArrayList<Sucursal> lista = gson.fromJson(response, new TypeToken<List<Sucursal>>() {
            }.getType());

            sucursalList = FXCollections.observableArrayList(lista);
            tblSucursal.setItems(sucursalList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mostrarDetalleSucursal() {
        Sucursal s = this.tblSucursal.getSelectionModel().getSelectedItem();

        if (s != null) {
            this.txtIdSala.setText(String.valueOf(s.getId()));
            this.txtNombre.setText(s.getNombre());
            this.txtDomicilio.setText(s.getDomicilio());
            this.txtLatitud.setText(String.valueOf(s.getLatitud()));
            this.txtLongitud.setText(String.valueOf(s.getLongitud()));
            this.txtEstatus.setText(String.valueOf(s.getEstatus()));
        }
    }

    public void limpiarFrmulario() {
        txtIdSala.setText("");
        txtNombre.setText("");
        txtDomicilio.setText("");
        txtLatitud.setText("");
        txtLongitud.setText("");
        txtEstatus.setText("");
    }

    @FXML
    private void txtFiltro(KeyEvent event) {

        String filtro = this.txtFiltro.getText();

        if (filtro.isEmpty()) {
            this.tblSucursal.setItems(sucursalList);
        } else {
            //Limpiamos la lista
            this.filtroSucursal.clear();

            for (Sucursal s : this.sucursalList) {
                if (s.getNombre().contains(filtro) || s.getDomicilio().contains(filtro)) {
                    this.filtroSucursal.add(s);
                }
            }

            this.tblSucursal.setItems(filtroSucursal);
        }

    }
}
