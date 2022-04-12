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
import org.scd.myspa.core.model.Producto;
/**
 * FXML Controller class
 *
 * @author zende
 */
public class ProductoController implements Initializable {

    @FXML private JFXTextField txtCodigo;
    @FXML private JFXTextField txtNombre;
    @FXML private JFXTextField txtMarca;
    @FXML private JFXTextField txtPrecioUso;
    @FXML private JFXTextField txtEstatus;
    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<?, ?> colCodigo;
    @FXML private TableColumn<?, ?> colNombre;
    @FXML private TableColumn<?, ?> colMarca;
    @FXML private TableColumn<?, ?> colPrecioUso;
    @FXML private TableColumn<?, ?> colEstatus;
    @FXML private JFXTextField txtFiltro;
    
    ObservableList<Producto> productosList;
    ObservableList<Producto> filtroProducto;

    // Generamos la instancia del objeto Client de javax.ws
    Client client = ClientBuilder.newClient();
    
    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         // Mapeado de columnas
        this.colCodigo.setCellValueFactory(new PropertyValueFactory("id"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        this.colPrecioUso.setCellValueFactory(new PropertyValueFactory("precioUso"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        
        ///Inicializamos la estructura de datos para poder utilizarla 
        productosList = FXCollections.observableArrayList();
        filtroProducto = FXCollections.observableArrayList();
        cargarTablaProductos();
    }
    public void saveProducto() {
        Producto p = new Producto();

        if (txtNombre.getText().equals("") || txtMarca.getText().equals("") || txtPrecioUso.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("¡¡¡ Alerta !!!");
            alert.setContentText("¡Ups... No se recibieron datos del producto para guardar!");
            alert.show();

        } else {
            p.setId(0);
            p.setNombre(txtNombre.getText());
            p.setMarca(txtMarca.getText());
            p.setPrecioUso(Double.parseDouble(txtPrecioUso.getText()));
            p.setEstatus(1);

            try {
                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
                Response response = client.target("http://localhost:8080/myspa/api/producto/save")
                        .queryParam("idProducto", p.getId())
                        .queryParam("nombre", p.getNombre())
                        .queryParam("marca", p.getMarca())
                        .queryParam("precioUso", p.getPrecioUso())
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.form(map));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro agregado correctamente");
                alert.setHeaderText("¡¡¡ Movimiento realizado !!!");
                alert.setContentText("¡Los datos del producto se han guardado correctamente!");
                alert.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

            cargarTablaProductos();
            limpiarFormulario();
        }
    }

    public void updateProducto() {
        Producto p = new Producto();

        if (txtNombre.getText().equals("") || txtMarca.getText().equals("") || txtPrecioUso.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("¡¡¡ Alerta !!!");
            alert.setContentText("¡Ups... No se recibieron datos del producto para guardar!");
            alert.show();

        } else {
            p.setId(Integer.parseInt(txtCodigo.getText())); 
            p.setNombre(txtNombre.getText());
            p.setMarca(txtMarca.getText());
            p.setPrecioUso(Double.parseDouble(txtPrecioUso.getText()));
            p.setEstatus(1);

            try {
                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
                Response response = client.target("http://localhost:8080/myspa/api/producto/save")
                        .queryParam("idProducto", p.getId())
                        .queryParam("nombre", p.getNombre())
                        .queryParam("marca", p.getMarca())
                        .queryParam("precioUso", p.getPrecioUso())
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.form(map));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro agregado correctamente");
                alert.setHeaderText("¡¡¡ Movimiento realizado !!!");
                alert.setContentText("¡Los datos del producto se han guardado correctamente!");
                alert.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

            cargarTablaProductos();
            limpiarFormulario();
        }
    }

    public void deleteProducto() {
        Producto p = new Producto();
        
        if (txtCodigo.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("¡¡¡ Alerta !!!");
            alert.setContentText("¡No se ha seleccionado ningún registro de producto para eliminar!");
            alert.show();
            
        } else {
            p.setId(Integer.parseInt(txtCodigo.getText())); 
            
            try {
                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
                Response response = client.target("http://localhost:8080/myspa/api/producto/delete")
                        .queryParam("idProducto", p.getId())
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.form(map));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro eliminado correctamente");
                alert.setHeaderText("¡¡¡ Producto eliminado !!!");
                alert.setContentText("¡El registro del producto se ha eliminado correctamente!");
                alert.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
            
            cargarTablaProductos();
            limpiarFormulario();
        }
    }
    
    public void cargarTablaProductos() {
        try {
            String response = client.target("http://localhost:8080/myspa/api/producto/getAll")
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            
            Gson gson = new Gson(); 
            
            ArrayList<Producto> lista = gson.fromJson(response, new TypeToken<List<Producto>>(){}.getType()); 
            
            productosList = FXCollections.observableArrayList(lista);
            tblProductos.setItems(productosList);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void filtrar(KeyEvent event) {
        String filtro = this.txtFiltro.getText();
        
        if (filtro.isEmpty()) {
            this.tblProductos.setItems(productosList);
            
        } else {
            // Limpiamos la lista
            this.filtroProducto.clear();
            
            for (Producto p : productosList) {
                if (String.valueOf(p.getId()).contains(filtro) || p.getNombre().contains(filtro) || p.getMarca().contains(filtro) || 
                        String.valueOf(p.getPrecioUso()).contains(filtro)) {
                    this.filtroProducto.add(p);
                    
                }
            }
            this.tblProductos.setItems(filtroProducto);
        }
    }
    
    public void mostratDetalleProductos() {
        Producto p = this.tblProductos.getSelectionModel().getSelectedItem();

        if (p != null) {
            this.txtCodigo.setText(String.valueOf(p.getId()));
            this.txtNombre.setText(p.getNombre());
            this.txtMarca.setText(p.getMarca());
            this.txtPrecioUso.setText(String.valueOf(p.getPrecioUso()));
            this.txtEstatus.setText(String.valueOf(p.getEstatus()));
        }
    }
    
    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecioUso.setText("");
        txtEstatus.setText("");
    }
}
