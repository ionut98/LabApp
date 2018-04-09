package com.company.Services;

import com.company.Domain.Nota;
import com.company.Domain.Student;
import com.company.Errors.RepositoryError;
import com.company.Repository.AbstractRepository;
import com.company.Utils.*;
import com.company.Utils.Observable;
import com.company.Utils.Observer;
import com.company.Validation.NotaValidator;
import com.company.Validation.ValidationException;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.company.Utils.SendEMail.sendEMail;

public class NotaService implements Service<Nota>,Observable<Nota> {

    private AbstractRepository<Nota> repository;
    private StudentService studentService;
    private TemaService temaService;
    private NotaValidator notaValidator;
    ArrayList<Observer<Nota>> noteObservers = new ArrayList<>();

    private Map<Integer,Integer> penalizariTeme = new TreeMap<>();
    private Map<Integer,Integer> penalizariStudenti = new TreeMap<>();


    private void initMaps(){
        File file1 = new File("StatisticiTeme.txt");
        File file2 = new File("StatisticiStudenti.txt");
        if(!file1.exists())
            try {
                file1.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        if(!file2.exists())
            try {
                file2.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        readStatisticsStudents();
        readStatisticsHomeWorks();
    }

    private void readStatisticsStudents() {
        penalizariStudenti.clear();
        Path path = Paths.get("StatisticiStudenti.txt");
        Stream<String> lines;
        try {
            lines= Files.lines(path);
            lines.forEach((line)->{
                String fields[] = line.split(";");
                int idStudent=Integer.parseInt(fields[0]);
                int nrPenalizari = Integer.parseInt(fields[1]);
                penalizariStudenti.put(idStudent,nrPenalizari);
            });
        } catch (IOException e) {
            System.out.println("");
        }
    }

    private void readStatisticsHomeWorks() {
        penalizariTeme.clear();
        Path path = Paths.get("StatisticiTeme.txt");
        Stream<String> lines;
        try {
            lines= Files.lines(path);
            lines.forEach((line)->{
                String fields[] = line.split(";");
                int idTema=Integer.parseInt(fields[0]);
                int nrPenalizari = Integer.parseInt(fields[1]);
                penalizariTeme.put(idTema,nrPenalizari);
            });
        } catch (IOException e) {
            System.out.println("");
        }
    }

    private void writeStudentsStats() throws IOException {

        Path path = Paths.get("StatisticiStudenti.txt");
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {

            penalizariStudenti.forEach((idStudent,nrPenalizari) -> {
                try {
                    bufferedWriter.write(idStudent+";"+nrPenalizari+"\n");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }


    private void writeHomeWorksStats() throws IOException {

        Path path = Paths.get("StatisticiTeme.txt");
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {

            penalizariTeme.forEach((idTema,nrPenalizari) -> {
                try {
                    bufferedWriter.write(idTema+";"+nrPenalizari+"\n");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    private void refreshMap(){
        studentService.getAll().forEach(student -> {if(!penalizariStudenti.containsKey(student.getIdStudent()))
                penalizariStudenti.put(student.getIdStudent(),0);});
        temaService.getAll().forEach(temaLaborator ->{if(!penalizariTeme.containsKey(temaLaborator.getNrTema()))
                penalizariTeme.put(temaLaborator.getNrTema(),0);});
    }

    private void addPenalty(Nota nota) throws IOException {
        refreshMap();
        int idTema=nota.getIdTema();
        int idStudent=nota.getIdStudent();
        int penalizareTema = penalizariTeme.get(idTema);
        int penalizareStudent = penalizariStudenti.get(idStudent);
        penalizariTeme.replace(idTema,penalizareTema,penalizareTema+1);
        penalizariStudenti.replace(idStudent,penalizareStudent,penalizareStudent+1);
        writeStudentsStats();
        writeHomeWorksStats();
    }


    public NotaService(AbstractRepository<Nota> repository, StudentService studentService, TemaService temaService, NotaValidator notaValidator) {
        this.repository = repository;
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaValidator = notaValidator;
        initMaps();
    }

    public Nota getNota(int idStudent, int idTema){
        for(Nota nota : repository.getAll())
            if(nota.getIdStudent()==idStudent &&
                    nota.getIdTema() == idTema)
                return nota;
        return null;
    }

    public boolean exists(Nota nota){
        for(Nota nota1 : repository.getAll())
            if(nota1.getIdStudent()==nota.getIdStudent() &&
                    nota1.getIdTema() == nota.getIdTema())
                return true;
        return false;

    }

    private void writeToFile(String FileName,String toWrite) throws IOException {

        try(BufferedWriter fout = new BufferedWriter(new FileWriter(FileName,true))) {
            try {
                fout.write(toWrite);
            }catch (IOException e){
                System.out.println("");
            }
        }catch (IOException e){
            System.out.println("");
        }
    }

    @Override
    public int size() {
        return repository.size();
    }

    @Override
    public void add(Nota nota) throws RepositoryError, ValidationException {
        notaValidator.validate(nota);
        if(studentService.exists(nota.getIdStudent()) && temaService.exists(nota.getIdTema())){
            repository.add(nota);
        }
        else
            throw new RepositoryError("IdStudent sau NrTema inexistent!");
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E element, final Iterable<E> list) {
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return list;
            }

            @Override
            public E getElement() {
                return element;
            }
        };

    }

    @Override
    public void update(Nota nota1, Nota nota2) throws RepositoryError, ValidationException {
        if(getNota(nota1.getIdStudent(),nota1.getIdTema())!=null) {
            notaValidator.validate(nota2);
            repository.update(nota1,nota2);
        }

    }

    @Override
    public void delete(Nota nota) throws RepositoryError {
        repository.delete(nota);
    }

    @Override
    public List<Nota> getAll() {
        return repository.getAll();
    }

    public void addNota(Nota nota, int saptPredare) throws RepositoryError, ValidationException, IOException {
        notaValidator.validate(nota);
        if(studentService.exists(nota.getIdStudent()) && temaService.exists(nota.getIdTema()) && !exists(nota)) {
            int deadLine = temaService.getDeadLine(nota.getIdTema());
            if(saptPredare <= deadLine) {
                repository.add(nota);
                ListEvent<Nota> event = createEvent(ListEventType.ADD,null,repository.getAll());
                notifyObservers(event);
                String record = "Adaugare nota--Nr Tema:" + nota.getIdTema()+"--Nota:"+
                        nota.getNota()+"--Deadline:"+ deadLine +"--A fost predata in saptamana:"+saptPredare+"\n";
                String numeFisier = nota.getIdStudent() +".txt";
                sendEMail(studentService.getEmail(nota.getIdStudent()),record);
                writeToFile(numeFisier,record);
            }
            else if(saptPredare-deadLine == 1) {
                nota.setNota(nota.getNota() - 2);
                repository.add(nota);
                ListEvent<Nota> event = createEvent(ListEventType.ADD,null,repository.getAll());
                notifyObservers(event);
                addPenalty(nota);
                String record = "Adaugare nota--Nr Tema:" + nota.getIdTema()+"--Nota:"+
                        nota.getNota()+"--Deadline:"+ deadLine +"--A fost predata in saptamana:"+saptPredare+"\n";
                String numeFisier = nota.getIdStudent() +".txt";
                sendEMail(studentService.getEmail(nota.getIdStudent()),record);
                writeToFile(numeFisier,record);
            }else if(saptPredare-deadLine == 2) {
                nota.setNota(nota.getNota() - 4);
                repository.add(nota);
                ListEvent<Nota> event = createEvent(ListEventType.ADD,null,repository.getAll());
                notifyObservers(event);
                addPenalty(nota);
                String record = "Adaugare nota--Nr Tema:" + nota.getIdTema()+"--Nota:"+
                        nota.getNota()+"--Deadline:"+ deadLine +"--A fost predata in saptamana:"+saptPredare+"\n";
                String numeFisier = nota.getIdStudent() +".txt";
                sendEMail(studentService.getEmail(nota.getIdStudent()),record);
                writeToFile(numeFisier,record);
            }
            else {
                nota.setNota(1);
                repository.add(nota);
                ListEvent<Nota> event = createEvent(ListEventType.ADD,null,repository.getAll());
                notifyObservers(event);
                addPenalty(nota);
                String record = "Adaugare nota--Nr Tema:" + nota.getIdTema()+"--Nota:"+
                        nota.getNota()+"--Deadline:"+ deadLine +"--A fost predata in saptamana:"+saptPredare+"\n";
                String numeFisier = nota.getIdStudent() +".txt";
                sendEMail(studentService.getEmail(nota.getIdStudent()),record);
                writeToFile(numeFisier,record);
            }
        }
        else
            throw new RepositoryError("IdStudent sau NrTema inexistent sau Nota deja existenta!");
    }

    public void updateNota(Nota nota1, Nota nota2, int saptPredare) throws ValidationException, RepositoryError, IOException {
        notaValidator.validate(nota2);
        if(studentService.exists(nota1.getIdStudent()) && temaService.exists(nota1.getIdTema())) {
            int deadLine = temaService.getDeadLine(nota1.getIdTema());
            if(saptPredare <= deadLine) {
                if (nota1.getNota() < nota2.getNota()) {
                    repository.update(nota1, nota2);
                    ListEvent<Nota> event = createEvent(ListEventType.UPDATE,nota1,repository.getAll());
                    notifyObservers(event);
                    String record = "Modificare nota--Nr Tema:" + nota2.getIdTema() + "--Nota:"
                            + nota2.getNota() + "--Deadline:" + deadLine + "--A fost predata in saptamana:" + saptPredare + "--Obs: Tema imbunatatita" + "\n";
                    String numeFisier = nota2.getIdStudent() + ".txt";
                    sendEMail(studentService.getEmail(nota2.getIdStudent()),record);
                    writeToFile(numeFisier, record);
                }
            }
            else if (saptPredare - deadLine == 1) {
                nota2.setNota(nota2.getNota() - 2);
                if (nota1.getNota() < nota2.getNota()) {
                    repository.update(nota1, nota2);
                    ListEvent<Nota> event = createEvent(ListEventType.UPDATE,nota1,repository.getAll());
                    notifyObservers(event);
                    addPenalty(nota2);
                    String record = "Modificare nota--Nr Tema:" + nota2.getIdTema() + "--Nota:"
                            + nota2.getNota() + "--Deadline:" + deadLine + "--A fost predata in saptamana:" + saptPredare + "--Obs: Tema imbunatatita. Deadline dapasit cu o saptamana" + "\n";
                    String numeFisier = nota2.getIdStudent() + ".txt";
                    sendEMail(studentService.getEmail(nota2.getIdStudent()),record);
                    writeToFile(numeFisier, record);
                }
            }
            else if (saptPredare - deadLine == 2) {
                nota2.setNota(nota2.getNota() - 4);
                if (nota1.getNota() < nota2.getNota()) {
                    repository.update(nota1, nota2);
                    ListEvent<Nota> event = createEvent(ListEventType.UPDATE,nota1,repository.getAll());
                    notifyObservers(event);
                    addPenalty(nota2);
                    String record = "Modificare nota--Nr Tema:" + nota2.getIdTema() + "--Nota:"
                            + nota2.getNota() + "--Deadline:" + deadLine + "--A fost predata in saptamana:" + saptPredare + "--Obs: Tema imbunatatita. Deadline dapasit cu doua saptamani" + "\n";
                    String numeFisier = nota2.getIdStudent() + ".txt";
                    sendEMail(studentService.getEmail(nota2.getIdStudent()),record);
                    writeToFile(numeFisier, record);
                }
            }
            else if(saptPredare-deadLine > 2) {
                nota2.setNota(1);
                if (nota1.getNota() < nota2.getNota()) {
                    repository.update(nota1, nota2);
                    ListEvent<Nota> event = createEvent(ListEventType.UPDATE,nota1,repository.getAll());
                    notifyObservers(event);
                    addPenalty(nota2);
                    String record = "Modificare nota--Nr Tema:" + nota2.getIdTema() + "--Nota:"
                            + nota2.getNota() + "--Deadline:" + deadLine + "--A fost predata in saptamana:" + saptPredare + "--Obs:Deadline dapasit cu doua saptamani" + "\n";
                    String numeFisier = nota2.getIdStudent() + ".txt";
                    sendEMail(studentService.getEmail(nota2.getIdStudent()),record);
                    writeToFile(numeFisier, record);

                }
            }
        }
        else
            throw new RepositoryError("IdStudent sau NrTema inexistent!");
    }

    public List<Nota> filtrareNota(float notaStudent){
        List<Nota> note = repository.getAll();
        return Filter.filterAndSorter(note, nota->nota.getNota()>notaStudent,(Nota nota1, Nota nota2)->{
            return nota1.compareTo(nota2);
        });
    }

    public List<Nota> filtrareIdStudent(int idStudent){
        List<Nota> note = repository.getAll();
        return Filter.filterAndSorter(note, nota->nota.getIdStudent()==idStudent,(Nota nota1, Nota nota2)->{
            return nota1.compareTo(nota2);
        });
    }

    public static Predicate<Nota> hasTheGradeAndTheLabId(float notaStudent, int idTema){
        return nota -> nota.getNota()>notaStudent &&
                nota.getIdTema() == idTema;
    }

    public List<Nota> filtrareIdTemaNota(int idTema, float notaStudent){
        List<Nota> note = repository.getAll();
        return Filter.filterAndSorter(note,hasTheGradeAndTheLabId(notaStudent,idTema),(Nota nota1, Nota nota2)->{
            return nota1.compareTo(nota2);
        });
    }

    public Map<Student,Float> statisticaNote(){

        Map<Student,Float>listaNote = new TreeMap<>();
        int nrTeme=temaService.getAll().size();
        studentService.getAll().forEach(student -> {
            float avg=0;
            int nrNote=0;
            for(int i=0;i<repository.getAll().size();i++)
                if(repository.getAll().get(i).getIdStudent()==student.getIdStudent()) {
                    avg += repository.getAll().get(i).getNota();
                    nrNote++;
                }
            if(nrNote==nrTeme)
                avg/=nrTeme;
            else {
                avg +=(nrTeme-nrNote);
                avg/=nrTeme;
            }
            listaNote.put(student,avg);});

        return listaNote;
    }

    public List<Integer> studentiFaraPenalitati(){

        List<Integer>studentiFaraPenalizari=new ArrayList<>();
        penalizariStudenti.forEach((StudentId,Penalizari)->{if(Penalizari==0)
                                    studentiFaraPenalizari.add(StudentId);});

        return studentiFaraPenalizari;
    }

    public Map<Integer,Integer> ceaMaiGreaTema(){

        Map<Integer,Integer> ceaMaiGreaTema=new TreeMap<>();

        List<Integer>listapenalizari = new ArrayList<>(penalizariTeme.values());
        int max=0;
        for (Integer valoare : listapenalizari)
            if(valoare>max)
                max=valoare;

        List<Integer>listaTeme = new ArrayList<>(penalizariTeme.keySet());
        for(Integer idTema : listaTeme)
            if(penalizariTeme.get(idTema)==max)
                ceaMaiGreaTema.put(idTema,max);

        return ceaMaiGreaTema;
    }

    @Override
    public void addObserver(Observer<Nota> o) {
        noteObservers.add(o);

    }

    @Override
    public void removeObserver(Observer<Nota> o) {
        noteObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Nota> event) {
        noteObservers.forEach(x->x.notifyEvent(event));
    }
}

