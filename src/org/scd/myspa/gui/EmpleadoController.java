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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import org.scd.myspa.core.model.Empleado;
import org.scd.myspa.core.model.Persona;
import org.scd.myspa.core.model.Usuario;

/**
 * FXML Controller class
 *
 * @author zende
 */
public class EmpleadoController implements Initializable {

    @FXML
    private JFXTextField txtIdEmpleado;
    @FXML
    private JFXTextField txtIdPersona;
    @FXML
    private JFXTextField txtIdUsuario;
    @FXML
    private JFXTextField txtNumeroEmpleado;
    @FXML
    private JFXTextField txtNombre;
    @FXML
    private JFXTextField txtApellidoPaterno;
    @FXML
    private JFXTextField txtApellidoMaterno;
    @FXML
    private JFXTextField txtDomicilio;
    @FXML
    private JFXTextField txtRutaFoto;
    @FXML
    private JFXTextField txtRfc;
    @FXML
    private JFXTextField txtTelefono;
    @FXML
    private JFXTextField txtEstatus;
    @FXML
    private JFXTextField txtNombreUsuario;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXTextField txtRol;
    @FXML
    private JFXTextField txtPuesto;

    @FXML
    private JFXButton btnGuardar;
    @FXML
    private JFXButton btnEliminar;
    @FXML
    private JFXButton btnNuevo;
    @FXML
    private JFXButton btnModificar;
    @FXML
    private JFXButton btnCargar;

    @FXML
    private TableView<Empleado> tblEmpleados;
    @FXML
    private TableColumn<?, ?> colIdEmpleado;
    @FXML
    private TableColumn<Empleado, String> colNombre;
    @FXML
    private TableColumn<Empleado, String> colGenero;
    @FXML
    private TableColumn<Empleado, String> colRFC;
    @FXML
    private TableColumn<Empleado, String> colDireccion;
    @FXML
    private TableColumn<?, ?> colEstatus;
    @FXML
    private TableColumn<?, ?> colPuesto;

    @FXML
    private JFXTextField txtFiltro;
    @FXML
    private ImageView imgEmpleado;
    @FXML
    ComboBox<String> cmbGenero;

    ObservableList<Empleado> empleadosList;
    private ObservableList<Empleado> filtroEmpleado;
    // Generamos la instancia del objeto Client de javax.ws
    Client client = ClientBuilder.newClient();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Mapeado de columnas
        this.colIdEmpleado.setCellValueFactory(new PropertyValueFactory("id"));
        this.colNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getNombre() + " "
                + param.getValue().getPersona().getApellidoPaterno() + " " + param.getValue().getPersona().getApellidoMaterno()));
        this.colGenero.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getGenero()));
        this.colRFC.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getRfc()));
        this.colDireccion.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getDomicilio()));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        this.colPuesto.setCellValueFactory(new PropertyValueFactory("puesto"));

        ObservableList<String> genero = FXCollections.observableArrayList(
                "M",
                "F",
                "O");
        cmbGenero.setItems(genero);
        ///Inicializamos la estructura de datos para poder utilizarla 
        empleadosList = FXCollections.observableArrayList();
        filtroEmpleado = FXCollections.observableArrayList();
        cargarTablaEmpleados();
    }

    public void saveDatosEmpleado() {

        Empleado cnt = this.tblEmpleados.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se encontraron datos para registrar al Empleado.");
            alert.showAndWait();
        }
        Persona p = new Persona();
        Empleado e = new Empleado();
        Usuario u = new Usuario();

        // Datos personales
        p.setId(0);
        p.setNombre(txtNombre.getText());
        p.setApellidoPaterno(txtApellidoPaterno.getText());
        p.setApellidoMaterno(txtApellidoMaterno.getText());
        p.setGenero(cmbGenero.getSelectionModel().getSelectedItem());
        p.setDomicilio(txtDomicilio.getText());
        p.setTelefono(txtTelefono.getText());
        p.setRfc(txtRfc.getText());

        // Datos de usuario
        u.setId(0);
        u.setNombreUsuario(txtNombreUsuario.getText());
        u.setContrasenia(txtPassword.getText());
        u.setRol("Empleado");

        // Datos de empleado
        e.setId(0);
        e.setRutaFoto(txtRutaFoto.getText());
        e.setEstatus(1);
        e.setPuesto(txtPuesto.getText());
        e.setNumeroEmpleado("");

        e.setPersona(p);
        e.setUsuario(u);

        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            map.add("empleado", new Gson().toJson(e));
            Response response = client.target("http://localhost:8080/myspa/api/empleado/save")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Empleado Agregado");
            alert.setContentText("El Empleado se registro corectamente.");
            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cargarTablaEmpleados();
        limpiarFormulario();
    }

    public void cargarFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Elige Una Imagen");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        Window stage = null;

        File imgFile = fileChooser.showOpenDialog(stage);

        if (imgFile != null) {
            Image image = new Image("file:" + imgFile.getAbsolutePath());
            imgEmpleado.setImage(image);
        }
    }

    public void cargarTablaEmpleados() {

        try {
            String response = client.target("http://localhost:8080/myspa/api/empleado/getAll")
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);

            Gson gson = new Gson();

            ArrayList<Empleado> lista = gson.fromJson(response, new TypeToken<List<Empleado>>() {
            }.getType());

            empleadosList = FXCollections.observableArrayList(lista);
            tblEmpleados.setItems(empleadosList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarDetalleEmpleados() {
        Empleado e = this.tblEmpleados.getSelectionModel().getSelectedItem();

        if (e != null) {
            this.txtIdEmpleado.setText(String.valueOf(e.getId()));
            this.txtIdPersona.setText(String.valueOf(e.getPersona().getId()));
            this.txtIdUsuario.setText(String.valueOf(e.getUsuario().getId()));
            this.txtNumeroEmpleado.setText(e.getNumeroEmpleado());
            this.txtNombre.setText(e.getPersona().getNombre());
            this.txtApellidoPaterno.setText(e.getPersona().getApellidoPaterno());
            this.txtApellidoMaterno.setText(e.getPersona().getApellidoMaterno());
            this.txtRfc.setText(e.getPersona().getRfc());
            this.txtTelefono.setText(e.getPersona().getTelefono());
            this.cmbGenero.getSelectionModel().select(e.getPersona().getGenero());
            this.txtDomicilio.setText(e.getPersona().getDomicilio());
            this.txtNombreUsuario.setText(e.getUsuario().getNombreUsuario());
            this.txtPassword.setText(e.getUsuario().getContrasenia());
            this.txtRol.setText(e.getUsuario().getRol());
            this.txtPuesto.setText(e.getPuesto());
            this.txtRutaFoto.setText(e.getRutaFoto());
            this.txtEstatus.setText(String.valueOf(e.getEstatus()));

        }
    }

    public void updateEmpleado() {

        Empleado cnt = this.tblEmpleados.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se encontraron datos para actualizar al Empleado.");
            alert.showAndWait();

        }
        Empleado e = new Empleado();
        Persona persona = new Persona();
        Usuario u = new Usuario();

        e.setNumeroEmpleado("");
        e.setPuesto(txtPuesto.getText());
        e.setEstatus(1);
        e.setRutaFoto(txtRutaFoto.getText());
        e.setId(txtIdEmpleado.getText().equals("") ? 0 : Integer.parseInt(txtIdEmpleado.getText()));
        persona.setId(txtIdPersona.getText().equals("") ? 0 : Integer.parseInt(txtIdPersona.getText()));
        u.setId(txtIdUsuario.getText().equals("") ? 0 : Integer.parseInt(txtIdUsuario.getText()));

        Empleado temp = null;

        for (Empleado emp : empleadosList) {

            if (emp.getId() == e.getId()) {

                temp = emp;
                persona.setId(temp.getPersona().getId());
                u.setId(temp.getUsuario().getId());
                break;
            }
        }
        persona.setNombre(txtNombre.getText());
        persona.setApellidoPaterno(txtApellidoPaterno.getText());
        persona.setApellidoMaterno(txtApellidoMaterno.getText());
        persona.setDomicilio(txtDomicilio.getText());
        persona.setGenero(cmbGenero.getSelectionModel().getSelectedItem());
        persona.setRfc(txtRfc.getText());
        persona.setTelefono(txtTelefono.getText());

        u.setNombreUsuario(txtNombreUsuario.getText());
        u.setContrasenia(txtPassword.getText());
        u.setRol("Empleado");

        e.setPersona(persona);
        e.setUsuario(u);

        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            map.add("empleado", new Gson().toJson(e));
            Response response = client.target("http://localhost:8080/myspa/api/empleado/save")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Empleado Actualizado");
            alert.setContentText("El Empleado se actualizó corectamente.");
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cargarTablaEmpleados();
        limpiarFormulario();

    }

    public void eliminarEmpleado() {
        Empleado cnt = this.tblEmpleados.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se encontraron datos para actualizar al Empleado.");
            alert.showAndWait();

        }

        Empleado empleado = new Empleado();

        if (txtNumeroEmpleado.getText() != "") {

            //Imprimo los datos en el campo para que estos puedan ser editados
            empleado.setId(Integer.parseInt(txtIdEmpleado.getText()));

            try {

                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();

                Response response = client.target("http://localhost:8080/myspa/api/empleado/delete2")
                        .queryParam("idEmpleado", empleado.getId())
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .post(Entity.form(map));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Empleado Eliminado");
                alert.setContentText("El Empleado se eliminó corectamente.");

            } catch (Exception e) {

                e.printStackTrace();
            }

            //Se carga de nuevo la tabla con los datos actualizados
            cargarTablaEmpleados();
            //Limpio los campos del formulario
            limpiarFormulario();
        }
    }

    public void limpiarFormulario() {
        txtIdEmpleado.setText("");
        txtIdPersona.setText("");
        txtIdUsuario.setText("");
        txtNumeroEmpleado.setText("");
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtRfc.setText("");
        txtTelefono.setText("");
        cmbGenero.getSelectionModel().clearSelection();
        txtDomicilio.setText("");
        txtNombreUsuario.setText("");
        txtPassword.setText("");
        txtRol.setText("");
        txtPuesto.setText("");
        txtEstatus.setText("");
        txtRutaFoto.setText("");

    }

    @FXML
    private void txtFiltro(KeyEvent event) {

        String filtro = this.txtFiltro.getText();

        if (filtro.isEmpty()) {
            this.tblEmpleados.setItems(empleadosList);
        } else {
            //Limpiamos la lista
            this.filtroEmpleado.clear();

            for (Empleado e : this.empleadosList) {
                if (e.getPersona().getNombre().contains(filtro) || e.getPersona().getRfc().contains(filtro)) {

                    this.filtroEmpleado.add(e);
                }
            }

            this.tblEmpleados.setItems(filtroEmpleado);
        }

    }
}
