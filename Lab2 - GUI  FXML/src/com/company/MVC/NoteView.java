package com.company.MVC;

import com.company.Domain.Nota;
import com.company.Repository.AbstractRepository;
import com.company.Repository.FileRepositoryStudents;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class NoteView  {

    private NoteController ctrl;
    private Scene scene1, scene2;
    private Stage stage;
    private BorderPane borderPane;
    TableView<Nota> tableView = new TableView<>();

    TextField idStudentField = createField();
    TextField idTemaField = createField();
    TextField notaField = createField();
    TextField saptPredareField = createField();

    Button buttonAdd = createButton("Add");
    Button buttonUpdate = createButton("Update");
    Button buttonClearFields = createButton("Clear all");

    public NoteView(NoteController ctrl) throws IOException {
        this.ctrl = ctrl;
        initView();
    }


    public BorderPane getView() {
        return borderPane;
    }

    private TextField createField(){
        TextField textField = new TextField();
        textField.getStylesheets().add("Style1.css");
        return textField;
    }

    private Button createButton(String name){
        Button button = new Button(name);
        button.getStylesheets().add("Style1.css");
        return button;

    }

    private Label createLabel(String labelName){
        Label label = new Label(labelName);
        label.getStylesheets().add("Style1.css");
        return label;
    }

    private GridPane createGridPane(){

        //labels
        GridPane gridPane = new GridPane();
        gridPane.add(createLabel("Id Student"),0,0);
        gridPane.add(createLabel("Id Tema"),0,1);
        gridPane.add(createLabel("Nota"),0,2);
        gridPane.add(createLabel("Saptamana predare"),0,3);


        //textFields
        gridPane.add(idStudentField,1,0);
        gridPane.add(idTemaField,1,1);
        gridPane.add(notaField,1,2);
        gridPane.add(saptPredareField,1,3);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(100d);

        ColumnConstraints c2 = new ColumnConstraints();
        c1.setPrefWidth(200d);

        gridPane.getColumnConstraints().addAll(c2,c2);

        return gridPane;
    }

    private HBox createButtonsMenu(){
        HBox hBox = new HBox();

        hBox.getChildren().addAll(buttonAdd,buttonUpdate,buttonClearFields);
        buttonAdd.setOnAction(ctrl::handleAddNota);
        buttonUpdate.setOnAction(ctrl::handleUpdateNota);
       // buttonClearFields.setOnAction(ctrl::handleClearFields);

        return hBox;
    }

    private TableView<Nota> createTableView(){

        tableView.getStylesheets().add("Style1.css");

        TableColumn<Nota, String> columnIdStudent = new TableColumn<>("Id Student");
        TableColumn<Nota, String> columnIdTema = new TableColumn<>("Id Tema");
        TableColumn<Nota, String> columnNota = new TableColumn<>("Nota");
        tableView.getColumns().addAll(columnIdStudent,columnIdTema,columnNota);

        columnIdStudent.setCellValueFactory(new PropertyValueFactory<Nota, String>("idStudent"));
        columnIdTema.setCellValueFactory(new PropertyValueFactory<Nota, String>("idTema"));
        columnNota.setCellValueFactory(new PropertyValueFactory<Nota, String>("nota"));

        tableView.setItems(ctrl.getModel());

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Nota>() {
            @Override
            public void changed(ObservableValue<? extends Nota> observable, Nota oldValue, Nota newValue) {
                ctrl.showNotaDetails(newValue);
            }
        });

        return tableView;
    }

    private AnchorPane initCenter(){

        AnchorPane centerPane = new AnchorPane();
        GridPane gridPane = createGridPane();
        centerPane.getChildren().add(gridPane);
        HBox buttonsMenu = createButtonsMenu();
        centerPane.getChildren().addAll(buttonsMenu);
        ImageView img = new ImageView(new Image("a.png"));

        img.setFitHeight(160);
        img.setFitWidth(240);

        centerPane.getChildren().add(img);
        AnchorPane.setTopAnchor(gridPane,30d);
        AnchorPane.setLeftAnchor(gridPane,70d);
        AnchorPane.setTopAnchor(buttonsMenu,230d);
        AnchorPane.setBottomAnchor(buttonsMenu,275d);
        AnchorPane.setLeftAnchor(buttonsMenu,75d);
        AnchorPane.setBottomAnchor(img,100d);
        AnchorPane.setLeftAnchor(img,100d);
        return centerPane;
    }

    private AnchorPane initTop(){

        AnchorPane topAnchorPane = new AnchorPane();

        Label titleLabel = new Label("Asignare note");

        topAnchorPane.getChildren().add(titleLabel);
        AnchorPane.setTopAnchor(titleLabel,20d);
        AnchorPane.setLeftAnchor(titleLabel,280d);
        titleLabel.setFont(new Font("Cambria",35));
        return topAnchorPane;
    }

    private AnchorPane initLeft(){
        AnchorPane leftAnchorPane = new AnchorPane();

        TableView<Nota> tableView = createTableView();
        tableView.autosize();
        leftAnchorPane.getChildren().add(tableView);
        AnchorPane.setTopAnchor(tableView,20d);
        AnchorPane.setLeftAnchor(tableView,20d);

        return leftAnchorPane;
    }


    private void initView(){
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-image: url(\"background.png\");-fx-background-size: 500, 500;");
        borderPane.setCenter(initCenter());
        borderPane.setTop(initTop());
        borderPane.setLeft(initLeft());

    }

}
