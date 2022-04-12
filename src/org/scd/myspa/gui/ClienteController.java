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
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
import org.scd.myspa.core.model.Cliente;
import org.scd.myspa.core.model.Persona;
import org.scd.myspa.core.model.Usuario;

/**
 * FXML Controller class
 *
 * @author zende
 */
public class ClienteController implements Initializable {

    @FXML
    private JFXTextField txtDireccion;
    @FXML
    private TableView<Cliente> tblCliente;
    @FXML
    private TableColumn<Cliente, ?> colId;

    @FXML
    private TableColumn<Cliente, String> colNombre;

    @FXML
    private TableColumn<Cliente, String> colRFC;

    @FXML
    private TableColumn<Cliente, String> colDomicilio;

    @FXML
    private TableColumn<?, ?> colEstatus;

    @FXML
    private JFXComboBox<String> cmbGenero;

    @FXML
    private JFXTextField txtRFC;

    @FXML
    private JFXTextField txtTelefono;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtApePaterno;

    @FXML
    private JFXTextField txtApeMaterno;

    @FXML
    private JFXTextField txtIdClie;

    @FXML
    private JFXTextField txtIdPersona;

    @FXML
    private JFXTextField txtIdUsuario;

    @FXML
    private JFXTextField txtCorreo;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnNuevo;

    @FXML
    private JFXButton btnModificar;

    @FXML
    private JFXTextField txtUsuario;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtRol;
    @FXML
    private JFXTextField txtFiltro;
    @FXML
    private JFXTextField txtEstatus;
    @FXML
    private JFXTextField txtRutaFoto;

    @FXML
    private JFXTextField txtNumCliente;

    @FXML
    private ImageView imgCliente;

    @FXML
    private JFXButton btnCargar;

    //Definir una estructura de datos para almacenar la lista de nuestros clientes 
    ObservableList<Cliente> listaClientes;
    private ObservableList<Cliente> filtroCliente;

    //Generamos la instancia de nuestro objeto Client de javax.ws
    Client client = ClientBuilder.newClient();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> genero = FXCollections.observableArrayList(
                "M",
                "F",
                "O");
        cmbGenero.setItems(genero);

        // Mapeado de columnas
        this.colId.setCellValueFactory(new PropertyValueFactory("id"));
        this.colNombre.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getNombre() + " "
                + param.getValue().getPersona().getApellidoPaterno() + " " + param.getValue().getPersona().getApellidoMaterno()));
        this.colRFC.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getRfc()));
        this.colDomicilio.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPersona().getDomicilio()));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));

        ///Inicializamos la estructura de datos para poder utilizarla 
        listaClientes = FXCollections.observableArrayList();
        filtroCliente = FXCollections.observableArrayList();
        cargarTablaCliete();

    }

    public void saveDatosCliente() {
        Cliente cnt = this.tblCliente.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se encontraron datos para registrar al cliente.");
            alert.showAndWait();
        }

        Persona p = new Persona();
        Usuario u = new Usuario();
        Cliente c = new Cliente();

        // Datos personales
        p.setId(0);
        p.setNombre(txtNombre.getText());
        p.setApellidoPaterno(txtApePaterno.getText());
        p.setApellidoMaterno(txtApeMaterno.getText());
        p.setGenero(cmbGenero.getSelectionModel().getSelectedItem());
        p.setDomicilio(txtDireccion.getText());
        p.setTelefono(txtTelefono.getText());
        p.setRfc(txtRFC.getText());

        // Datos de usuario
        u.setId(0);
        u.setNombreUsuario(txtUsuario.getText());
        u.setContrasenia(txtPassword.getText());
        u.setRol("Cliente");

        //Datos del Cliente
        c.setId(0);
        c.setEstatus(1);
        c.setCorreo(txtCorreo.getText());
        c.setRutaFoto(txtRutaFoto.getText());
        c.setNumeroUnico("");

        c.setPersona(p);
        c.setUsuario(u);

        try {
            MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
            map.add("cliente", new Gson().toJson(c));
            Response response = client.target("http://localhost:8080/myspa/api/cliente/save")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.form(map));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Cliente Agregado");
            alert.setContentText("El Cliente se registro corectamente.");
            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cargarTablaCliete();
        limpiarFormulario();
    }

    public void updateDatosCliente() {
        Cliente cnt = this.tblCliente.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se seleccionó ningún cliente para actualizar.");
            alert.showAndWait();
        } else {

            Persona p = new Persona();
            Usuario u = new Usuario();
            Cliente c = new Cliente();

            c.setNumeroUnico("");
            c.setEstatus(1);
            c.setCorreo(txtCorreo.getText());
            c.setId(txtIdClie.getText().equals("") ? 0 : Integer.parseInt(txtIdClie.getText()));
            p.setId(txtIdPersona.getText().equals("") ? 0 : Integer.parseInt(txtIdPersona.getText()));
            u.setId(txtIdUsuario.getText().equals("") ? 0 : Integer.parseInt(txtIdUsuario.getText()));

            Cliente t = null;

            for (Cliente cl : listaClientes) {
                if (cl.getId() == c.getId()) {
                    t = cl;
                    p.setId(t.getPersona().getId());
                    u.setId(t.getUsuario().getId());
                    break;
                }

            }

            p.setNombre(txtNombre.getText());
            p.setApellidoPaterno(txtApePaterno.getText());
            p.setApellidoMaterno(txtApeMaterno.getText());
            p.setDomicilio(txtDireccion.getText());
            p.setGenero(cmbGenero.getSelectionModel().getSelectedItem());
            p.setRfc(txtRFC.getText());
            p.setTelefono(txtTelefono.getText());

            u.setNombreUsuario(txtUsuario.getText());
            u.setContrasenia(txtPassword.getText());
            u.setRol("Cliente");

            c.setPersona(p);
            c.setUsuario(u);

            try {
                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
                map.add("cliente", new Gson().toJson(c));
                Response response = client.target("http://localhost:8080/myspa/api/cliente/save")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.form(map));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Cliente Modificado");
                alert.setContentText("El Cliente se actualizó corectamente.");
                alert.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            this.tblCliente.refresh();
            cargarTablaCliete();
            limpiarFormulario();

        }
    }

    public void deletDatosCliente() {
        Cliente cnt = this.tblCliente.getSelectionModel().getSelectedItem();
        if (cnt == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("ERROR");
            alert.setContentText("No se seleccionó ningún registro para eliminar.");
            alert.showAndWait();
        }
        Cliente c = new Cliente();

        if (txtNumCliente.getText() != "") {
            //Imprimo los datos en el campo para que estos puedan ser editados
            c.setId(Integer.parseInt(txtIdClie.getText()));
            try {

                MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();

                Response response = client.target("http://localhost:8080/myspa/api/cliente/delete2")
                        .queryParam("id", c.getId())
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .post(Entity.form(map));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Cliente Eliminado");
                alert.setContentText("El Cliente se eliminó corectamente.");
                alert.showAndWait();
            } catch (Exception e) {

                e.printStackTrace();
            }

            this.tblCliente.refresh();
            cargarTablaCliete();

            limpiarFormulario();
        }
    }

    public void cargarTablaCliete() {
        try {
            //Aquí se hace la peticón al servidor y guardamos la respuesta en la variable RESPONSE
            String response = client.target("http://localhost:8080/myspa/api/cliente/getAll")
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);

            Gson gson = new Gson();

            ArrayList<Cliente> lista = gson.fromJson(response, new TypeToken<List<Cliente>>() {
            }.getType());
            listaClientes = FXCollections.observableArrayList(lista);
            tblCliente.setItems(listaClientes);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            imgCliente.setImage(image);
        }
    }

    public void mostrarDetalleCliente() {
        Cliente c = this.tblCliente.getSelectionModel().getSelectedItem();

        if (c != null) {
            this.txtIdClie.setText(String.valueOf(c.getId()));
            this.txtIdPersona.setText(String.valueOf(c.getPersona().getId()));
            this.txtIdUsuario.setText(String.valueOf(c.getUsuario().getId()));
            this.txtNombre.setText(c.getPersona().getNombre());
            this.txtApePaterno.setText(c.getPersona().getApellidoPaterno());
            this.txtApeMaterno.setText(c.getPersona().getApellidoMaterno());
            this.cmbGenero.getSelectionModel().select(c.getPersona().getGenero());
            this.txtRFC.setText(c.getPersona().getRfc());
            this.txtTelefono.setText(c.getPersona().getTelefono());
            this.txtCorreo.setText(c.getCorreo());
            this.txtDireccion.setText(c.getPersona().getDomicilio());
            this.txtUsuario.setText(c.getUsuario().getNombreUsuario());
            this.txtPassword.setText(c.getUsuario().getContrasenia());
            this.txtRol.setText(c.getUsuario().getRol());
            this.txtEstatus.setText(String.valueOf(c.getEstatus()));
            this.txtNumCliente.setText(c.getNumeroUnico());
            this.txtRutaFoto.setText(c.getRutaFoto());
        }

    }

    public void limpiarFormulario() {
        txtIdClie.setText("");
        txtIdPersona.setText("");
        txtIdUsuario.setText("");
        txtCorreo.setText("");
        txtNombre.setText("");
        txtApePaterno.setText("");
        txtApeMaterno.setText("");
        txtRFC.setText("");
        txtTelefono.setText("");
        cmbGenero.getSelectionModel().clearSelection();
        txtDireccion.setText("");
        txtUsuario.setText("");
        txtPassword.setText("");
        txtRol.setText("");
        txtEstatus.setText("");
        txtNumCliente.setText("");
        txtRutaFoto.setText("");

    }

    @FXML
    void txtFiltro(KeyEvent event) {
        String filtro = this.txtFiltro.getText();

        if (filtro.isEmpty()) {
            this.tblCliente.setItems(listaClientes);
        } else {
            //Limpiamos la lista
            this.filtroCliente.clear();

            for (Cliente c : this.listaClientes) {
                if (c.getPersona().getNombre().contains(filtro) || c.getPersona().getRfc().contains(filtro)) {

                    this.filtroCliente.add(c);
                }
            }

            this.tblCliente.setItems(filtroCliente);
        }
    }
}
