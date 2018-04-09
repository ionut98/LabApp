package com.company.MVC;

import com.company.Domain.Student;
import com.company.Errors.RepositoryError;
import com.company.Services.StudentService;
import com.company.Utils.ListEvent;
import com.company.Utils.Observer;
import com.company.Validation.ValidationException;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentController implements Observer<Student> {

    private StudentService service;
    private ObservableList<Student> model = FXCollections.observableArrayList();


    @FXML
    private TableView tableView;

    @FXML
    private TableColumn columnId,columnNume,columnGrupa,columnEmail,columnProfesor;

    @FXML
    private ComboBox comboBoxFiltrare;

    @FXML
    private TextField textFieldId, textFieldNume, textFieldGrupa, textFieldEmail, textFieldProfesor, textFieldFiltrare;

    @FXML
    private Button buttonAdd, buttonDelete, buttonUpdate, buttonClear;

    @FXML
    private void initialize(){
        this.columnId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("idStudent"));
        this.columnNume.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        this.columnGrupa.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        this.columnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        this.columnProfesor.setCellValueFactory(new PropertyValueFactory<Student, String>("profesorIndrumator"));

        ObservableList filterList = FXCollections.observableArrayList("Grupa","Email","Profesor");
        this.comboBoxFiltrare.setItems(filterList);
        tableView.setItems(model);

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
                showStudentDetails(newValue);
            }
        });
    }

    public void StudentControllersetService(StudentService service) {
        this.service = service;
        this.model.setAll(service.getAll());
    }

    @Override
    public void notifyEvent(ListEvent<Student> listEvent) {
        model.setAll(StreamSupport.stream(listEvent.getList().spliterator(),false)
        .collect(Collectors.toList()));
    }

    public ObservableList<Student> getModel() {
        return model;
    }

    private Student getStudentAttributesFromFields(){
        if (this.textFieldId.getText().isEmpty() ||
                this.textFieldNume.getText().isEmpty() ||
                this.textFieldGrupa.getText().isEmpty() ||
                this.textFieldEmail.getText().isEmpty() ||
                this.textFieldProfesor.getText().isEmpty())
            showMessage(Alert.AlertType.ERROR,"Eroare","Campurile nu pot fi goale!");
       else {
            int id = Integer.parseInt(this.textFieldId.getText());
            String nume = this.textFieldNume.getText();
            int grupa = Integer.parseInt(this.textFieldGrupa.getText());
            String email = this.textFieldEmail.getText();
            String profesor = this.textFieldProfesor.getText();

            return new Student(id, nume, grupa, email, profesor);
        }
        return null;
    }

    public void handleAddStudent (ActionEvent actionEvent){

        if(getStudentAttributesFromFields() != null)
            try {
                service.add(getStudentAttributesFromFields());
                showMessage(Alert.AlertType.INFORMATION,"Salvare","Student salvat cu succes!");
            } catch (RepositoryError repositoryError) {
                showMessage(Alert.AlertType.ERROR,"Eroare",repositoryError.getMessage());
            } catch (ValidationException e) {
                showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
            }

            else
            showMessage(Alert.AlertType.ERROR,"Eroare","Reintroduceti datele!");
            this.model.setAll(service.getAll());
    }

    public void handleUpdateStudent (ActionEvent actionEvent){

        if(getStudentAttributesFromFields() != null)
        try {
            service.update(new Student(Integer.parseInt(this.textFieldId.getText())," ",0," "," "),getStudentAttributesFromFields());
            showMessage(Alert.AlertType.INFORMATION,"Update","Student modificat cu succes!");
        } catch (RepositoryError repositoryError) {
            showMessage(Alert.AlertType.ERROR,"Eroare",repositoryError.getMessage());
        } catch (ValidationException e) {
            showMessage(Alert.AlertType.ERROR,"Eroare",e.getMessage());
        }
        else
            showMessage(Alert.AlertType.ERROR,"Eroare","Reintroduceti datele!");
        this.model.setAll(service.getAll());

    }

    public void handleClearFields(){

        this.tableView.getSelectionModel().clearSelection();
        this.textFieldId.clear();
        this.textFieldId.setDisable(false);
        this.textFieldNume.clear();
        this.textFieldGrupa.clear();
        this.textFieldEmail.clear();
        this.textFieldProfesor.clear();

        this.textFieldFiltrare.clear();
        this.comboBoxFiltrare.getSelectionModel().clearSelection();
        this.model.setAll(service.getAll());

    }


    public void handleDeleteStudent(ActionEvent actionEvent){
        Student student = (Student) this.tableView.getSelectionModel().getSelectedItem();
        if(student != null)
        {
            try {
                service.delete(student);
                showMessage(Alert.AlertType.INFORMATION,"Stergere","Stergere efectuata!");
            } catch (RepositoryError repositoryError) {
                showMessage(Alert.AlertType.ERROR,"Eroare","Eroare la stergere");
            }
        }
        this.model.setAll(service.getAll());
    }

    public void handleFilter(ActionEvent actionEvent){
        if(!comboBoxFiltrare.getSelectionModel().isEmpty() && !textFieldFiltrare.getText().isEmpty()){
            if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Grupa") && textFieldFiltrare.getText().matches("[0-9]*")) {
                try {
                    List<Student> filtered = (List<Student>) service.filtrareGrupa(Integer.parseInt(textFieldFiltrare.getText().toString()));
                    this.model.setAll(filtered);
                }catch(Error error){
                    showMessage(Alert.AlertType.ERROR,"Eroare","Pentru filtrare dupa grupa, introduceti un numar!");
                    handleClearFields();
                }
            }
            else if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Email")){
                List<Student> filtered = (List<Student>) service.filtrareDomeniuEmail(textFieldFiltrare.getText().toString());
                this.model.setAll(filtered);
            }

            else if(comboBoxFiltrare.getSelectionModel().getSelectedItem().toString().equals("Profesor")){
                List<Student> filtered = (List<Student>) service.filtrareProfesor(textFieldFiltrare.getText().toString());
                this.model.setAll(filtered);
            }

        }

    }


    public void showStudentDetails(Student student){
        if(student!=null){
            this.textFieldId.setText(String.valueOf(student.getIdStudent()));
            this.textFieldId.setDisable(true);
            this.textFieldNume.setText(student.getNume());
            this.textFieldGrupa.setText(String.valueOf(student.getGrupa()));
            this.textFieldEmail.setText(student.getEmail());
            this.textFieldProfesor.setText(student.getProfesorIndrumator());
        }
    }

    static void showMessage(Alert.AlertType type, String header, String content){
        Alert message = new Alert(type);
        message.setHeaderText(header);
        message.setContentText(content);
        message.showAndWait();
    }

}
