package com.company.MVC;

import com.company.Domain.Nota;
import com.company.Services.NotaService;
import com.company.Services.StudentService;
import com.company.Services.TemaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private StudentService studentService;
    private TemaService temaService;
    private NotaService notaService;


    @FXML
    private Button buttonStudent,buttonTema,buttonNota,buttonStatistici;

    public void mainSetControllers(StudentService studentService, TemaService temaService, NotaService notaService){
        this.studentService=studentService;
        this.temaService=temaService;
        this.notaService=notaService;
    }

    public void handleButtonStudent(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage studentStage = new Stage();
            studentStage.setTitle("Studenti");
            studentStage.getIcons().add(new Image("35785-200.png"));
            Scene scene = new Scene(root);
            studentStage.setScene(scene);

            StudentController studentController = loader.getController();
            studentController.StudentControllersetService(studentService);
            studentService.addObserver(studentController);
            studentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonTema(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TemaView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage temaStage = new Stage();
            temaStage.setTitle("Teme");
            temaStage.getIcons().add(new Image("30591-200.png"));
            Scene scene = new Scene(root);
            temaStage.setScene(scene);

            TemaLabController temaLabController = loader.getController();
            temaLabController.TemaLabControllerSetService(temaService);
            temaService.addObserver(temaLabController);
            temaStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonNota(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NoteView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage notaStage = new Stage();
            notaStage.setTitle("Note");
            notaStage.getIcons().add(new Image("694075_test_512x512.png"));
            Scene scene = new Scene(root);
            notaStage.setScene(scene);

            NoteController noteController= loader.getController();
            noteController.NoteControllerSetService(notaService);
            notaService.addObserver(noteController);
            notaStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonStatistici(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticiView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage statsStage = new Stage();
            statsStage.setTitle("Statistici");
            statsStage.getIcons().add(new Image("16394-200.png"));
            Scene scene = new Scene(root);
            statsStage.setScene(scene);
/*
            NoteController noteController= loader.getController();
            noteController.NoteControllerSetService(notaService);
            notaService.addObserver(noteController);*/
            statsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
