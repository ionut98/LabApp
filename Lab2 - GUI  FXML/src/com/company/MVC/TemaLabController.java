package com.company.MVC;

import com.company.Domain.Student;
import com.company.Domain.TemaLaborator;
import com.company.Errors.RepositoryError;
import com.company.Services.StudentService;
import com.company.Services.TemaService;
import com.company.Utils.ListEvent;
import com.company.Utils.Observer;
import com.company.Validation.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TemaLabController implements Observer<TemaLaborator> {

    private TemaService service;
    private ObservableList<TemaLaborator> model = FXCollections.observableArrayList();

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn columnNrTema, columnCerinta, columnDeadline;

    @FXML
    private ComboBox comboBoxFiltrare;

    @FXML
    private TextField textFieldNrTema, textFieldDeadline, textFieldSaptamanaCurenta, textFieldFiltrare;

    @FXML
    private TextArea textAreaCerinta;

    @FXML
    private Button buttonAdd, buttonUpdateDeadline, buttonClearFields;


   @FXML
   private void initialize(){
       this.columnNrTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator,Integer>("nrTema"));
       this.columnCerinta.setCellValueFactory(new PropertyValueFactory<TemaLaborator,String>("cerinta"));
       this.columnDeadline.setCellValueFactory(new PropertyValueFactory<TemaLaborator,Integer>("deadline"));

        ObservableList filterList = FXCollections.observableArrayList("Cerinta","Deadline","Mixta");
        this.comboBoxFiltrare.setItems(filterList);
        tableView.setItems(model);

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TemaLaborator>() {
            @Override
            public void changed(ObservableValue <? extends TemaLaborator> observable, TemaLaborator oldValue, TemaLaborator newValue) {
                showTemaDetails(newValue);
            }
        });
   }


    public void  TemaLabControllerSetService (TemaService service) {
        this.service = service;
        this.model.setAll(service.getAll());
    }

    @Override
    public void notifyEvent(ListEvent<TemaLaborator> listEvent) {
        model.setAll(StreamSupport.stream(listEvent.getList().spliterator(),false)
                .collect(Collectors.toList()));
    }

    public ObservableList<TemaLaborator> getModel() {
        return model;
    }

    private TemaLaborator getTemaAttributesFromFields(){
        if (this.textFieldNrTema.getText().isEmpty() ||
                this.textAreaCerinta.getText().isEmpty() ||
                this.textFieldDeadline.getText().isEmpty())
            showMessage(Alert.AlertType.ERROR,"Eroare","Campurile nu pot fi goale!");
        else {
            int nrTema = Integer.parseInt(this.textFieldNrTema.getText());
            String cerinta = this.textAreaCerinta.getText();
            int deadline = Integer.parseInt(this.textFieldDeadline.getText());

            return new TemaLaborator(nrTema,deadline,cerinta);
        }
        return null;
    }

    public void handleAddTema (ActionEvent actionEvent){

        if(getTemaAttributesFromFields() != null)
            try {
                service.add(getTemaAttributesFromFields());
                showMessage(Alert.AlertType.INFORMATION,"Salvare","Tema salvata cu succes!");
            } catch (RepositoryError repositoryError) {
                showMessage(Alert.AlertType.ERROR,"Eroare",repositoryError.getMessage());
            } catch (ValidationException e) {
                showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
            }

        else
            showMessage(Alert.AlertType.ERROR,"Eroare","Reintroduceti datele!");
            this.model.setAll(service.getAll());
    }

    public void handleUpdateDeadlineTema (ActionEvent actionEvent){

        if(this.textFieldNrTema.getText().isEmpty() ||
                this.textFieldDeadline.getText().isEmpty()||
                this.textFieldSaptamanaCurenta.getText().isEmpty())
            showMessage(Alert.AlertType.ERROR,"Eroare","Campurile nu pot fi goale!");
        else
            try {
                service.updateDeadline(Integer.parseInt(this.textFieldNrTema.getText()),Integer.parseInt(this.textFieldDeadline.getText()),Integer.parseInt(this.textFieldSaptamanaCurenta.getText()));
                showMessage(Alert.AlertType.INFORMATION,"Update deadline","Deadline modificat cu succes!");
            } catch (RepositoryError repositoryError) {
                showMessage(Alert.AlertType.ERROR,"Eroare",repositoryError.getMessage());
            }
            this.model.setAll(service.getAll());
    }


    public void handleFilter(ActionEvent actionEvent){
        if(!comboBoxFiltrare.getSelectionModel().isEmpty() && !textFieldFiltrare.getText().isEmpty()){
            if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Cerinta")) {
                    List<TemaLaborator> filtered = (List<TemaLaborator>) service.filtrareCerinta(textFieldFiltrare.getText().toString());
                    this.model.setAll(filtered);
                    comboBoxFiltrare.getSelectionModel().clearSelection();
                    textFieldFiltrare.clear();
                }
            else if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Deadline")){
                List<TemaLaborator> filtered = (List<TemaLaborator>) service.filtrareDeadline(Integer.parseInt(textFieldFiltrare.getText().toString()));
                this.model.setAll(filtered);
                comboBoxFiltrare.getSelectionModel().clearSelection();
                textFieldFiltrare.clear();
            }

            else if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Mixta")){
                String [] rec = textFieldFiltrare.getText().toString().split(";");
                List<TemaLaborator> filtered = (List<TemaLaborator>) service.filtrareCerintaDeadline(rec[0],Integer.parseInt(rec[1]));
                this.model.setAll(filtered);
                comboBoxFiltrare.getSelectionModel().clearSelection();
                textFieldFiltrare.clear();
            }

        }

    }


    public void handleClearFields(){

        this.tableView.getSelectionModel().clearSelection();

        this.textFieldNrTema.clear();
        this.textFieldNrTema.setDisable(false);
        this.textAreaCerinta.clear();
        this.textFieldDeadline.clear();
        this.textFieldSaptamanaCurenta.clear();

        this.textFieldFiltrare.clear();
        this.comboBoxFiltrare.getSelectionModel().clearSelection();
        this.model.setAll(service.getAll());
    }

    public void showTemaDetails(TemaLaborator temaLaborator){
        if(temaLaborator!=null){
            this.textFieldNrTema.setText(String.valueOf(temaLaborator.getNrTema()));
            this.textFieldNrTema.setDisable(true);
            this.textAreaCerinta.setText(temaLaborator.getCerinta());
            this.textFieldDeadline.setText(String.valueOf(temaLaborator.getDeadline()));
        }
    }

    static void showMessage(Alert.AlertType type, String header, String content){
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(content);
        message.showAndWait();
    }

}
