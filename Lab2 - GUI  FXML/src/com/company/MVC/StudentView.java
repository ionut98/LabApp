package com.company.MVC;
import com.company.Domain.Student;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class StudentView {

    private StudentController ctrl;
    //private Scene scene2,scene3;
    //private Stage stage;
    private BorderPane borderPane;
    TableView<Student> tableView = new TableView<>();

    TextField idField = createField();
    TextField numeField = createField();
    TextField grupaField = createField();
    TextField emailField = createField();
    TextField profesorField = createField();

    Button buttonAdd = createButton("Add");
    Button buttonUpdate = createButton("Update");
    Button buttonDelete = createButton("Delete");
    Button buttonClearFields = createButton("Clear all");


    public StudentView(StudentController ctrl) {
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
        gridPane.add(createLabel("Id"),0,0);
        gridPane.add(createLabel("Nume"),0,1);
        gridPane.add(createLabel("Grupa"),0,2);
        gridPane.add(createLabel("e-Mail"),0,3);
        gridPane.add(createLabel("Profesor"),0,4);

        //textFields
        gridPane.add(idField,1,0);
        gridPane.add(numeField,1,1);
        gridPane.add(grupaField,1,2);
        gridPane.add(emailField,1,3);
        gridPane.add(profesorField,1,4);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(100d);

        ColumnConstraints c2 = new ColumnConstraints();
        c1.setPrefWidth(200d);

        gridPane.getColumnConstraints().addAll(c2,c2);

        return gridPane;
    }

    private HBox createButtonsMenu(){
        HBox hBox = new HBox();

        hBox.getChildren().addAll(buttonAdd,buttonUpdate,buttonDelete,buttonClearFields);
        buttonAdd.setOnAction(ctrl::handleAddStudent);
        buttonUpdate.setOnAction(ctrl::handleUpdateStudent);
        buttonDelete.setOnAction(ctrl::handleDeleteStudent);
 //       buttonClearFields.setOnAction(ctrl::handleClearFields);

        return hBox;
    }


    private TableView<Student> createTableView(){

        tableView.getStylesheets().add("Style1.css");

        TableColumn<Student, String> columnId = new TableColumn<>("Id");
        TableColumn<Student, String> columnNume = new TableColumn<>("Nume");
        TableColumn<Student, String> columnGrupa = new TableColumn<>("Grupa");
        TableColumn<Student, String> columnEmail = new TableColumn<>("eMail");
        TableColumn<Student, String> columnProf = new TableColumn<>("Profesor");
        tableView.getColumns().addAll(columnId,columnNume,columnGrupa,columnEmail,columnProf);

        columnId.setCellValueFactory(new PropertyValueFactory<Student, String>("idStudent"));
        columnNume.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        columnGrupa.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        columnProf.setCellValueFactory(new PropertyValueFactory<Student, String>("profesorIndrumator"));

        tableView.setItems(ctrl.getModel());

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student oldValue, Student newValue) {
               // ctrl.showStudentDetails(newValue);
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
        ImageView img = new ImageView(new Image("stud.png"));

        img.setFitHeight(160);
        img.setFitWidth(240);

        centerPane.getChildren().add(img);
        AnchorPane.setTopAnchor(gridPane,30d);
        AnchorPane.setLeftAnchor(gridPane,70d);
        AnchorPane.setTopAnchor(buttonsMenu,230d);
        AnchorPane.setBottomAnchor(buttonsMenu,275d);
        AnchorPane.setLeftAnchor(buttonsMenu,50d);
        AnchorPane.setBottomAnchor(img,100d);
        AnchorPane.setLeftAnchor(img,100d);
        return centerPane;
    }

    private AnchorPane initTop(){

        AnchorPane topAnchorPane = new AnchorPane();

        Label titleLabel = new Label("Studenti Management");

        topAnchorPane.getChildren().add(titleLabel);
        AnchorPane.setTopAnchor(titleLabel,20d);
        AnchorPane.setLeftAnchor(titleLabel,280d);
        titleLabel.setFont(new Font("Cambria",35));
        return topAnchorPane;
    }

    private AnchorPane initLeft(){
        AnchorPane leftAnchorPane = new AnchorPane();

        TableView<Student> tableView = createTableView();
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
