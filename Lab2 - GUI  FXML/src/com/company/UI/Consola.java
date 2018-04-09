package com.company.UI;

import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Domain.TemaLaborator;
import com.company.Errors.RepositoryError;
import com.company.Services.NotaService;
import com.company.Services.StudentService;
import com.company.Services.TemaService;
import com.company.Validation.ValidationException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Consola {

    private Scanner scanner;
    private NotaService notaService;
    private StudentService studentService;
    private TemaService temaService;
    private MenuCommand MeniuPrincipal;

    public Consola(StudentService studentService, TemaService temaService,NotaService notaService) {
        this.scanner = new Scanner(System.in);
        this.notaService = notaService;
        this.studentService = studentService;
        this.temaService = temaService;
    }

    public class ExitCommand implements Command {

        @Override
        public void execute() {
            System.exit(0);
        }
    }

    public class AdaugareStudent implements Command {

        @Override
        public void execute() {
            int idStudent, grupa;
            String nume, email, profesor;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Adaugare Student*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti id-ul studentului: ");
                idStudent = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti numele studentului: ");
                nume = scanner.nextLine();

                System.out.println("Introduceti grupa: ");
                grupa = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti email: ");
                email = scanner.nextLine();

                System.out.println("Introduceti numele profesorului: ");
                profesor = scanner.nextLine();

                studentService.add(new Student(idStudent, nume, grupa, email, profesor));
                System.out.println("\n\t|Adaugare efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }
        }
    }

    public class ModificareStudent implements Command {

        @Override
        public void execute() {
            int idStudent1, idStudent2, grupa2;
            String nume2, email2, profesor2;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Modificare Student*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti id-ul studentului de modificat: ");
                idStudent1 = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti id-ul  noului student: ");
                idStudent2 = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti numele studentului: ");
                nume2 = scanner.nextLine();

                System.out.println("Introduceti grupa: ");
                grupa2 = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti email: ");
                email2 = scanner.nextLine();

                System.out.println("Introduceti numele profesorului: ");
                profesor2 = scanner.nextLine();

                studentService.update(new Student(idStudent1, "", 0, "", ""), new Student(idStudent2, nume2, grupa2, email2, profesor2));
                for(Nota nota : notaService.getAll())
                    if(nota.getIdStudent() == idStudent1)
                        notaService.delete(nota);

                System.out.println("\n\t|Modificare efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }
        }

    }

    public class StergereStudent implements Command {

        @Override
        public void execute() {
            int idStudent;
            String nume;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Stergere Student*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti id-ul studentului de sters: ");
                idStudent = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti numele studentului: ");
                nume = scanner.nextLine();

                studentService.delete(new Student(idStudent, nume, 0, "", ""));
                for (Nota nota : notaService.getAll())
                    if (nota.getIdStudent() == idStudent)
                        notaService.delete(nota);

                System.out.println("\n\t|Stergere efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }
        }

    }

    public class AdaugareTema implements Command {

        @Override
        public void execute() {
            int nrTema, deadline;
            String descriere;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Adaugare Tema*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti nr-ul temei: ");
                nrTema = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti descriere: ");
                descriere = scanner.nextLine();

                System.out.println("Introduceti deadline: ");
                deadline = Integer.parseInt(scanner.nextLine());

                temaService.add(new TemaLaborator(nrTema,deadline,descriere));
                System.out.println("\n\t|Adaugare efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }
        }
    }

    public class PrelungireDeadlineTema implements Command {

        @Override
        public void execute() {
            int nrTema, deadline, saptCurenta;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Prelungire Deadline Tema*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti nr-ul temei: ");
                nrTema = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti saptamana curenta: ");
                saptCurenta = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti noul deadline: ");
                deadline = Integer.parseInt(scanner.nextLine());

                temaService.updateDeadline(nrTema,deadline,saptCurenta);
                System.out.println("\n\t|Modificare deadline efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }
        }
    }

    public class StergereTema implements Command {

        @Override
        public void execute() {
            int nrTema;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Stergere Tema*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti nr-ul temei de sters: ");
                nrTema = Integer.parseInt(scanner.nextLine());

                temaService.delete(new TemaLaborator(nrTema,0,""));
                for (Nota nota : notaService.getAll())
                    if (nota.getIdTema() == nrTema)
                        notaService.delete(nota);

                System.out.println("\n\t|Stergere efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }
        }

    }

    public class AdaugareNota implements Command {

        @Override
        public void execute() {
            int nrTema,idStudent;
            float valoare;
            int saptPredare;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Adaugare Nota*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti id-ul studentului: ");
                idStudent = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti nr-ul temei: ");
                nrTema = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti nota: ");
                valoare = Float.parseFloat(scanner.nextLine());

                System.out.println("Introduceti saptamana predare: ");
                saptPredare = Integer.parseInt(scanner.nextLine());

                notaService.addNota(new Nota(idStudent,nrTema,valoare),saptPredare);

                System.out.println("\n\t|Adaugare efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }catch (IOException ioe){
                System.out.println("");
            }

        }
    }


    public class ModificareNota implements Command {

        @Override
        public void execute() {
            int nrTema,idStudent;
            float valoare;
            int saptPredare;

            Scanner scanner = new Scanner(System.in);
            try {
                System.out.println("~*~********Modificare Nota*********~*~");
                System.out.println("\n");

                System.out.println("Introduceti id-ul studentului: ");
                idStudent = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti nr-ul temei: ");
                nrTema = Integer.parseInt(scanner.nextLine());

                System.out.println("Introduceti noua nota: ");
                valoare = Float.parseFloat(scanner.nextLine());

                System.out.println("Introduceti saptamana predare: ");
                saptPredare = Integer.parseInt(scanner.nextLine());

                Nota nota1 = notaService.getNota(idStudent,nrTema);
                notaService.updateNota(new Nota(idStudent,nrTema,nota1.getNota()),new Nota(idStudent,nrTema,valoare),saptPredare);

                System.out.println("\n\t|Modificare efectuata|\n");
            } catch (RepositoryError repositoryError) {
                System.out.println(repositoryError.getMessage());
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(ime.getMessage());
            }catch (IOException ioe){
                System.out.println("");
            }

        }
    }

    public class AfiseazaStudenti implements Command{

        @Override
        public void execute() {
            System.out.println("~~*~~IdStudent|Nume|Grupa|Email|Profesor~~*~~");
            System.out.println("_____________________________________________");
        for(Student st:studentService.getAll())
            System.out.println(st);
            System.out.println("_____________________________________________");
        }
    }

    public class AfiseazaTeme implements Command{

        @Override
        public void execute() {
            System.out.println("~~*~~NrTema|Deadline|Descriere~~*~~");
            System.out.println("_____________________________________________");
            for(TemaLaborator temaLaborator:temaService.getAll())
                System.out.println(temaLaborator);
            System.out.println("_____________________________________________");
        }
    }

    public class AfiseazaNote implements Command{

        @Override
        public void execute() {
            System.out.println("~~*~~IdStudent|NrTema|Nota~~*~~");
            System.out.println("_____________________________________________");
            for(Nota nota :notaService.getAll())
                System.out.println(nota);
            System.out.println("_____________________________________________");
        }
    }

    public class FiltrareGrupa implements Command {

        @Override
        public void execute() {
            List<Student> listaFiltrata;
            int grupa;
            System.out.println("Nr Grupei: ");
            grupa = Integer.parseInt(scanner.nextLine());
            int exists = 0;
            for (Student student : studentService.getAll()) {
                if (student.getGrupa() == grupa)
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista aceasta grupa!!");
            } else {
                System.out.println("~~*~~IdStudent|Nume|NrGrupa|eMail|Profesor~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata = studentService.filtrareGrupa(grupa);
                listaFiltrata.forEach(st-> System.out.println(st));
                System.out.println("_____________________________________________");
            }
        }
    }

        public class FiltrareDomeniu implements Command{

            @Override
            public void execute() {
                String domeniu;
                List<Student>listaFiltrata;
                System.out.println("Domeniu email: ");
                domeniu = scanner.nextLine();
                int exists=0;
                for(Student student:studentService.getAll())
                {
                    if(student.getEmail().contains(domeniu))
                        exists=1;
                }
                if(exists==0) {
                    System.out.println("Nu exista studenti cu acest domeniu de email!!");
                }
                else {
                    System.out.println("~~*~~IdStudent|Nume|NrGrupa|eMail|Profesor~~*~~");
                    System.out.println("_____________________________________________");
                    listaFiltrata=studentService.filtrareDomeniuEmail(domeniu);
                    listaFiltrata.forEach(st-> System.out.println(st));
                    System.out.println("_____________________________________________");
                }
            }

    }

    public class FiltrareProfesor implements Command {

        @Override
        public void execute() {
            List<Student>listaFiltrata;
            String profesor;
            System.out.println("Nume Profesor Indrumator: ");
            profesor = scanner.nextLine();
            int exists = 0;
            for (Student student : studentService.getAll()) {
                if (student.getProfesorIndrumator().equals(profesor))
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista studenti cu acest profesor indrumator!!");
            } else {
                System.out.println("~~*~~IdStudent|Nume|NrGrupa|eMail|Profesor~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata=studentService.filtrareProfesor(profesor);
                listaFiltrata.forEach(st-> System.out.println(st));
                System.out.println("_____________________________________________");
            }
        }
    }

    public class FiltrareCerinta implements Command {

        @Override
        public void execute() {
            String termenCheie;
            List<TemaLaborator>listaFiltrata;
            System.out.println("TermenCheie: ");
            termenCheie = scanner.nextLine();
            int exists = 0;
            for (TemaLaborator temaLaborator : temaService.getAll()) {
                if (temaLaborator.getCerinta().contains(termenCheie))
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista teme cu asemenea cerinta!!");
            } else {
                System.out.println("~~*~~NrTema|Deadline|Cerinta~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata=temaService.filtrareCerinta(termenCheie);
                listaFiltrata.forEach(tema-> System.out.println(tema));
                System.out.println("_____________________________________________");
            }
        }
    }

    public class FiltrareDeadline implements Command {

        @Override
        public void execute() {
            int deadline;
            List<TemaLaborator>listaFiltrata;
            System.out.println("Deadline: ");
            deadline = Integer.parseInt(scanner.nextLine());
            int exists = 0;
            for (TemaLaborator temaLaborator : temaService.getAll()) {
                if (temaLaborator.getDeadline() < deadline)
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista teme cu deadline mai mic decat cel introdus!!");
            } else {
                System.out.println("~~*~~NrTema|Deadline|Cerinta~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata=temaService.filtrareDeadline(deadline);
                listaFiltrata.forEach(tema-> System.out.println(tema));
                System.out.println("_____________________________________________");
            }
        }
    }

    public class FiltrareCerintaDeadline implements Command {

        @Override
        public void execute() {
            List<TemaLaborator>listaFiltrata;
            int deadline;
            String termenCheie;
            System.out.println("Termen Cheie: ");
            termenCheie = scanner.nextLine();

            System.out.println("Deadline: ");
            deadline = Integer.parseInt(scanner.nextLine());
            int exists = 0;
            for (TemaLaborator temaLaborator : temaService.getAll()) {
                if (temaLaborator.getDeadline() < deadline && temaLaborator.getCerinta().toLowerCase().contains(termenCheie.toLowerCase()))
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista teme cu deadline mai mic decat cel introdus si care sa contina acest termen cheie!!");
            } else {
                System.out.println("~~*~~NrTema|Deadline|Cerinta~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata=temaService.filtrareCerintaDeadline(termenCheie,deadline);
                listaFiltrata.forEach(tema-> System.out.println(tema));
                System.out.println("_____________________________________________");
            }
        }
    }

    public class FiltrareNota implements Command {

        @Override
        public void execute() {
            List<Nota> listaFiltrata;
            float nota;
            System.out.println("Nota prag: ");
            nota = Float.parseFloat(scanner.nextLine());
            int exists = 0;
            for (Nota nota1: notaService.getAll()) {
                if (nota1.getNota() > nota)
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista inregistrari valabile!!");
            } else {
                System.out.println("~~*~~IdStudent|NrTema|Nota~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata=notaService.filtrareNota(nota);
                listaFiltrata.forEach(n-> System.out.println(n));
                System.out.println("_____________________________________________");
            }
        }
    }

    public class FiltrareIdStudent implements Command {

        @Override
        public void execute() {
            List<Nota>listaFiltrata;
            int idStudent;
            System.out.println("IdStudent: ");
            idStudent = Integer.parseInt(scanner.nextLine());
            int exists = 0;
            for (Nota nota1: notaService.getAll()) {
                if (nota1.getIdStudent() == idStudent)
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista inregistrari valabile!!");
            } else {
                System.out.println("~~*~~IdStudent|NrTema|Nota~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata=notaService.filtrareIdStudent(idStudent);
                listaFiltrata.forEach(n-> System.out.println(n));
                System.out.println("_____________________________________________");
            }
        }
    }


    public class FiltrareIdTemaNota implements Command {

        @Override
        public void execute() {
            List<Nota>listaFiltrata;
            int idTema;
            float nota;
            System.out.println("IdTema: ");
            idTema = Integer.parseInt(scanner.nextLine());

            System.out.println("Nota: ");
            nota = Float.parseFloat(scanner.nextLine());

            int exists = 0;
            for (Nota nota1: notaService.getAll()) {
                if (nota1.getIdTema() == idTema && nota1.getNota()>nota)
                    exists = 1;
            }
            if (exists == 0) {
                System.out.println("Nu exista inregistrari valabile!!");
            } else {
                System.out.println("~~*~~IdStudent|NrTema|Nota~~*~~");
                System.out.println("_____________________________________________");
                listaFiltrata=notaService.filtrareIdTemaNota(idTema,nota);
                listaFiltrata.forEach(n-> System.out.println(n));
                System.out.println("_____________________________________________");
            }
        }

    }


    public class StatisticaNote implements Command {

        @Override
        public void execute() {
            Map<Student,Float> listaNote;

            System.out.println("~~*~~Nume|Medie~~*~~");
            System.out.println("_____________________________________________");
            listaNote=notaService.statisticaNote();
            listaNote.forEach((s,n)-> {System.out.println(s.getNume());
                System.out.println(n);});
            System.out.println("_____________________________________________");
        }
    }


    public class StatisticaStudentiPromovati implements Command {

        @Override
        public void execute() {
            Map<Student,Float> listaNote;

            System.out.println("~~*~~Nume|Medie~~*~~");
            System.out.println("_____________________________________________");
            listaNote=notaService.statisticaNote();
            listaNote.forEach((s,n)-> {
                if(n>=4){
                System.out.println(s.getNume());
                System.out.println(n);
                }
            });
            System.out.println("_____________________________________________");
        }
    }


    public class  CeaMaiGreaTema implements Command {

        @Override
        public void execute() {
            Map<Integer,Integer> listaTeme;

            System.out.println("~~*~~Tema|Numar Penalizari~~*~~");
            System.out.println("_____________________________________________");
            listaTeme=notaService.ceaMaiGreaTema();
            listaTeme.forEach((t,p)-> {
                    System.out.println(temaService.getById(t));
                    System.out.println(p);
                }
            );
            System.out.println("_____________________________________________");
        }
    }


    public class  StudentiFaraPenalitati implements Command {

        @Override
        public void execute() {
            List<Integer>listaStudenti;

            System.out.println("~~*~~Studenti~~*~~");
            System.out.println("_____________________________________________");
            listaStudenti=notaService.studentiFaraPenalitati();
            listaStudenti.forEach(idStudent-> System.out.println(studentService.getStudent(idStudent)));
            System.out.println("_____________________________________________");
        }
    }

    private void CreateMenu(){

        MeniuPrincipal = new MenuCommand("_____Meniu Principal_____");

        MenuCommand StudentMenu = new MenuCommand("~~*~~Meniu Studenti~~*~~");
        StudentMenu.addCommand("1.Adaugare Student",new AdaugareStudent());
        StudentMenu.addCommand("2.Modificare Student",new ModificareStudent());
        StudentMenu.addCommand("3.Stergere Student",new StergereStudent());
        StudentMenu.addCommand("4.Afisare Studenti",new AfiseazaStudenti());
        StudentMenu.addCommand("5.Filtrare dupa Grupa",new FiltrareGrupa());
        StudentMenu.addCommand("6.Filtrare dupa Domeniu eMail",new FiltrareDomeniu());
        StudentMenu.addCommand("7.Filtrare dupa Profesor Indrumator",new FiltrareProfesor());
        StudentMenu.addCommand("<--Revenire meniu principal", MeniuPrincipal);

        MenuCommand TemaMenu = new MenuCommand("~~*~~Meniu Teme~~*~~");
        TemaMenu.addCommand("1.Adaugare Tema",new AdaugareTema());
        TemaMenu.addCommand("2.Modificare Deadline Tema",new PrelungireDeadlineTema());
        TemaMenu.addCommand("3.Stergere Tema",new StergereTema());
        TemaMenu.addCommand("4.Afisare Teme",new AfiseazaTeme());
        TemaMenu.addCommand("5.Filtrare dupa Cerinta(termen cheie)",new FiltrareCerinta());
        TemaMenu.addCommand("6.Filtrare dupa Deadline(termen limita)",new FiltrareDeadline());
        TemaMenu.addCommand("7.Filtrare dupa Deadline si Cerinta(termen limita + termen cheie)",new FiltrareCerintaDeadline());
        TemaMenu.addCommand("<--Revenire meniu principal",MeniuPrincipal);

        MenuCommand NotaMenu = new MenuCommand("~~*~~Meniu Note~~*~~");
        NotaMenu.addCommand("1.Adaugare Nota",new AdaugareNota());
        NotaMenu.addCommand("2.Modificare Nota",new ModificareNota());
        NotaMenu.addCommand("3.Afisare Note",new AfiseazaNote());
        NotaMenu.addCommand("4.Filtrare dupa nota prag",new FiltrareNota());
        NotaMenu.addCommand("5.Filtrare dupa IdStudent",new FiltrareIdStudent());
        NotaMenu.addCommand("6.Filtrare dupa IdTema si Nota",new FiltrareIdTemaNota());
        NotaMenu.addCommand("<--Revenire meniu principal",MeniuPrincipal);

        MenuCommand StatisticiMenu = new MenuCommand("~~*~~Meniu Statistici~~*~~");
        StatisticiMenu.addCommand("1.Note laborator",new StatisticaNote());
        StatisticiMenu.addCommand("2.Cea/cele mai grea/grele tema/teme",new CeaMaiGreaTema());
        StatisticiMenu.addCommand("3.Studentii care pot intra in examen",new StatisticaStudentiPromovati());
        StatisticiMenu.addCommand("4.Studentii care au predat toate laboratoarele la timp",new StudentiFaraPenalitati());
        StatisticiMenu.addCommand("<--Revenire meniu principal", MeniuPrincipal);


        MeniuPrincipal.addCommand("1.Exit", new ExitCommand());
        MeniuPrincipal.addCommand("2.Meniu Note", NotaMenu);
        MeniuPrincipal.addCommand("3.Meniu Studenti",StudentMenu);
        MeniuPrincipal.addCommand("4.Meniu Teme", TemaMenu);
        MeniuPrincipal.addCommand("5.Meniu Statistici", StatisticiMenu);

    }

    public void runMenu(){

        CreateMenu();
        MenuCommand meniuCurent = MeniuPrincipal;

        while(true){
        try {
            System.out.println(meniuCurent.getMenuName());

            meniuCurent.execute();
            System.out.println("-----------------------");
            System.out.println("Introduceti optiunea: ");
            int optiune = Integer.parseInt(scanner.nextLine());

            if (optiune >= 0 && optiune <= meniuCurent.getCommands().size()) {
                Command comandaSelectata = meniuCurent.getCommands().get(optiune-1);
                if (comandaSelectata instanceof MenuCommand)
                    meniuCurent = (MenuCommand) comandaSelectata;
                else
                    comandaSelectata.execute();
            }

        }catch (InputMismatchException e){
            System.out.println(e.getMessage());
        }

        }

    }

}
