package com.company.MVC;

import com.company.Domain.Student;
import com.company.Domain.TemaLaborator;
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

public class TemaLabView  {

    private TemaLabController ctrl;
    private Scene scene1, scene3;
    private Stage stage;
    private BorderPane borderPane;
    TableView<TemaLaborator> tableView = new TableView<>();


    TextField nrTemaField = createField();
    TextArea cerintaField = new TextArea();
    TextField deadlineField = createField();
    TextField saptCurenta = createField();

    Button buttonAdd = createButton("Add");
    Button buttonUpdateDeadline = createButton("Update Deadline");
    Button buttonClearFields = createButton("Clear all");

    public TemaLabView(TemaLabController ctrl) {
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
        gridPane.add(createLabel("Nr Tema"),0,0);
        gridPane.add(createLabel("Cerinta"),0,1);
        gridPane.add(createLabel("Deadline"),0,2);
        gridPane.add(createLabel("Saptamana Curenta"),0,4);

        //textFields
        gridPane.add(nrTemaField,1,0);
        gridPane.add(cerintaField,1,1);
        gridPane.add(deadlineField,1,2);
        gridPane.add(saptCurenta,1,4);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(100d);

        ColumnConstraints c2 = new ColumnConstraints();
        c1.setPrefWidth(200d);

        gridPane.getColumnConstraints().addAll(c2,c2);

        return gridPane;
    }

    private HBox createButtonsMenu(){
        HBox hBox = new HBox();

        hBox.getChildren().addAll(buttonAdd,buttonUpdateDeadline,buttonClearFields);
        buttonAdd.setOnAction(ctrl::handleAddTema);
        buttonUpdateDeadline.setOnAction(ctrl::handleUpdateDeadlineTema);
        //buttonClearFields.setOnAction(ctrl::handleClearFields);

        return hBox;
    }

    private TableView<TemaLaborator> createTableView(){

        tableView.getStylesheets().add("Style1.css");

        TableColumn<TemaLaborator, String> columnNrTema = new TableColumn<>("Nr Tema");
        TableColumn<TemaLaborator, String> columnCerinta = new TableColumn<>("Cerinta");
        TableColumn<TemaLaborator, String> columnDeadline = new TableColumn<>("Deadline");
        tableView.getColumns().addAll(columnNrTema,columnCerinta,columnDeadline);

        columnNrTema.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("nrTema"));
        columnCerinta.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("cerinta"));
        columnDeadline.setCellValueFactory(new PropertyValueFactory<TemaLaborator, String>("deadline"));

        tableView.setItems(ctrl.getModel());

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TemaLaborator>() {
            @Override
            public void changed(ObservableValue<? extends TemaLaborator> observable, TemaLaborator oldValue, TemaLaborator newValue) {
                ctrl.showTemaDetails(newValue);
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

        AnchorPane.setTopAnchor(gridPane,30d);
        AnchorPane.setLeftAnchor(gridPane,70d);
        //AnchorPane.setTopAnchor(buttonsMenu,320d);
        AnchorPane.setBottomAnchor(buttonsMenu,50d);
        AnchorPane.setLeftAnchor(buttonsMenu,250d);
        //AnchorPane.setBottomAnchor(img,75d);
        //AnchorPane.setLeftAnchor(img,100d);
        return centerPane;
    }

    private AnchorPane initTop(){

        AnchorPane topAnchorPane = new AnchorPane();

        Label titleLabel = new Label("Teme de Laborator");

        topAnchorPane.getChildren().add(titleLabel);
        AnchorPane.setTopAnchor(titleLabel,20d);
        AnchorPane.setLeftAnchor(titleLabel,280d);
        titleLabel.setFont(new Font("Cambria",35));
        return topAnchorPane;
    }

    private AnchorPane initLeft(){
        AnchorPane leftAnchorPane = new AnchorPane();

        TableView<TemaLaborator> tableView = createTableView();
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
