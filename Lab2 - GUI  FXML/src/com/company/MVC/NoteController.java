package com.company.MVC;

import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Domain.TemaLaborator;
import com.company.Errors.RepositoryError;
import com.company.Repository.AbstractRepository;
import com.company.Repository.FileRepositoryLabs;
import com.company.Repository.FileRepositoryStudents;
import com.company.Services.NotaService;
import com.company.Services.StudentService;
import com.company.Services.TemaService;
import com.company.Utils.ListEvent;
import com.company.Utils.Observer;
import com.company.Validation.StudentValidator;
import com.company.Validation.TemaValidator;
import com.company.Validation.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NoteController implements Observer<Nota> {

    private NotaService service;
    private ObservableList<Nota> model = FXCollections.observableArrayList();

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn columnId, columnNr, columnNota;

    @FXML
    private ComboBox comboBoxFiltrare;

    @FXML
    private TextField textFieldId, textFieldNr, textFieldNota, textFieldSaptamanaPredare, textFieldFiltrare;

    @FXML
    private Button buttonAdd, buttonUpdate, buttonClearFields;


    @FXML
    private void initialize(){
        this.columnId.setCellValueFactory(new PropertyValueFactory<Nota,Integer>("idStudent"));
        this.columnNr.setCellValueFactory(new PropertyValueFactory<Nota,Integer>("idTema"));
        this.columnNota.setCellValueFactory(new PropertyValueFactory<Nota,Float>("nota"));

        ObservableList filterList = FXCollections.observableArrayList("Nota","Student","Mixta");
        this.comboBoxFiltrare.setItems(filterList);
        tableView.setItems(model);

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Nota>() {
            @Override
            public void changed(ObservableValue<? extends Nota> observable, Nota oldValue, Nota newValue) {
                showNotaDetails(newValue);
            }
        });

    }

    public void NoteControllerSetService(NotaService service) {
        this.service = service;
        this.model.setAll(service.getAll());
    }


    @Override
    public void notifyEvent(ListEvent<Nota> listEvent) {
        model.setAll(StreamSupport.stream(listEvent.getList().spliterator(),false)
                .collect(Collectors.toList()));
    }

    public ObservableList<Nota> getModel() {
        return model;
    }

    private Nota getNotaAttributesFromFields(){
        if (this.textFieldId.getText().isEmpty() ||
                this.textFieldNr.getText().isEmpty() ||
                this.textFieldNota.getText().isEmpty())
            showMessage(Alert.AlertType.ERROR,"Eroare","Campurile nu pot fi goale!");
        else {
            int idStudent = Integer.parseInt(this.textFieldId.getText());
            int idTema = Integer.parseInt(this.textFieldNr.getText());
            float nota = Float.parseFloat(this.textFieldNota.getText());

            return new Nota(idStudent, idTema,nota);
        }
        return null;
    }

    public void handleAddNota (ActionEvent actionEvent){


            try {
                service.addNota(getNotaAttributesFromFields(),Integer.parseInt(this.textFieldSaptamanaPredare.getText()));
                showMessage(Alert.AlertType.INFORMATION,"Salvare","Nota salvata cu succes! Notificare trimisa!");
            } catch (RepositoryError repositoryError) {
                showMessage(Alert.AlertType.ERROR,"Eroare",repositoryError.getMessage());
            } catch (ValidationException e) {
                showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
            } catch (IOException e) {
                //e.printStackTrace();
            }
            this.model.setAll(service.getAll());
    }

    public void handleUpdateNota (ActionEvent actionEvent){

        if(getNotaAttributesFromFields() != null)
            try {
                Nota nota1 = service.getNota(Integer.parseInt(this.textFieldId.getText()),Integer.parseInt(this.textFieldNr.getText()));
                service.updateNota(nota1,getNotaAttributesFromFields(),Integer.parseInt(this.textFieldSaptamanaPredare.getText()));
                showMessage(Alert.AlertType.INFORMATION,"Update","Nota modificata cu succes! Notificare trimisa!");
            } catch (RepositoryError repositoryError) {
                showMessage(Alert.AlertType.ERROR,"Eroare",repositoryError.getMessage());
            } catch (ValidationException e) {
                showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
            } catch (IOException e) {
                //e.printStackTrace();
            }
        else
            showMessage(Alert.AlertType.ERROR,"Eroare","Reintroduceti datele!");

            this.model.setAll(service.getAll());

    }


    public void handleFilter(ActionEvent actionEvent){
        if(!comboBoxFiltrare.getSelectionModel().isEmpty() && !textFieldFiltrare.getText().isEmpty()){
            if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Nota")) {
                List<Nota> filtered = (List<Nota>) service.filtrareNota(Float.parseFloat(textFieldFiltrare.getText().toString()));
                this.model.setAll(filtered);
                comboBoxFiltrare.getSelectionModel().clearSelection();
                textFieldFiltrare.clear();
            }
            else if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Student")){
                List<Nota> filtered = (List<Nota>) service.filtrareIdStudent(Integer.parseInt(textFieldFiltrare.getText().toString()));
                this.model.setAll(filtered);
                comboBoxFiltrare.getSelectionModel().clearSelection();
                textFieldFiltrare.clear();
            }

            else if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Mixta")){
                String [] rec = textFieldFiltrare.getText().toString().split(";");
                List<Nota> filtered = (List<Nota>) service.filtrareIdTemaNota(Integer.parseInt(rec[0]),Float.parseFloat(rec[1]));
                this.model.setAll(filtered);
                comboBoxFiltrare.getSelectionModel().clearSelection();
                textFieldFiltrare.clear();
            }

        }

    }

    public void handleClearFields(){

        this.tableView.getSelectionModel().clearSelection();

        this.textFieldId.setDisable(false);
        this.textFieldId.clear();
        this.textFieldNr.setDisable(false);
        this.textFieldNr.clear();
        this.textFieldNota.clear();
        this.textFieldSaptamanaPredare.clear();

        this.textFieldFiltrare.clear();
        this.comboBoxFiltrare.getSelectionModel().clearSelection();
        this.model.setAll(service.getAll());
    }

    public void showNotaDetails(Nota nota){
        if(nota!=null){
            this.textFieldId.setText(String.valueOf(nota.getIdStudent()));
            this.textFieldId.setDisable(true);
            this.textFieldNr.setText(String.valueOf(nota.getIdTema()));
            this.textFieldNr.setDisable(true);
            this.textFieldNota.setText(String.valueOf(nota.getNota()));
        }
    }



    static void showMessage(Alert.AlertType type, String header, String content){
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(content);
        message.showAndWait();
    }
}
