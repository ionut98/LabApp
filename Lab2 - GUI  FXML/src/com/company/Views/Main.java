package com.company.Views;

import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Domain.TemaLaborator;
import com.company.MVC.*;
import com.company.Repository.AbstractRepository;
import com.company.Repository.FileRepositoryGrades;
import com.company.Repository.FileRepositoryLabs;
import com.company.Repository.FileRepositoryStudents;
import com.company.Services.NotaService;
import com.company.Services.StudentService;
import com.company.Services.TemaService;
import com.company.Validation.NotaValidator;
import com.company.Validation.StudentValidator;
import com.company.Validation.TemaValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    AbstractRepository<Student> repoStudenti = new FileRepositoryStudents("Studenti.txt");
    AbstractRepository<TemaLaborator> repoTeme = new FileRepositoryLabs("Teme.txt");
    AbstractRepository<Nota> repoNote = new FileRepositoryGrades("Catalog.txt");

    StudentValidator studentValidator = new StudentValidator();
    TemaValidator temaValidator = new TemaValidator();
    NotaValidator notaValidator = new NotaValidator();

    StudentService serviceStudenti = new StudentService(repoStudenti,studentValidator);
    TemaService serviceTeme = new TemaService(repoTeme,temaValidator);
    NotaService serviceNote = new NotaService(repoNote,serviceStudenti,serviceTeme,notaValidator);


    public Main() throws IOException {
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LabApp");
        primaryStage.getIcons().add(new Image("hw.png"));

        final FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        SplitPane root = loader.load();
        MainController mainController = loader.getController();
        mainController.mainSetControllers(serviceStudenti,serviceTeme,serviceNote);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
