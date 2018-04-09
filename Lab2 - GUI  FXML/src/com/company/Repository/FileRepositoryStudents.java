package com.company.Repository;

import com.company.Domain.Student;
import com.company.Errors.RepositoryError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

public class FileRepositoryStudents extends RepositoryInMemory<Student> {

    File file;

    private void readFromFile(){
        entities.clear();
        String relativePath = file.getName();
        Path path = Paths.get(relativePath);
        Stream<String>lines;
        try {
            lines= Files.lines(path);
            lines.forEach((st)->{
                String fields[] = st.split(";");
                int idStudent=Integer.parseInt(fields[0]);
                String nume = fields[1];
                int grupa = Integer.parseInt(fields[2]);
                String email = fields[3];
                String prof = fields[4];
                entities.add(new Student(idStudent,nume,grupa,email,prof));
            });
        } catch (IOException e) {
            System.out.println("");
        }

    }

    private void writeToFile() throws IOException {
        String relativePath = file.getName();
        Path path = Paths.get(relativePath);
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {

            entities.forEach(st -> {
                try {
                    bufferedWriter.write(st.getIdStudent()+";"+st.getNume()+";"+st.getGrupa()+";"+st.getEmail()+";"+st.getProfesorIndrumator()+"\n");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    public FileRepositoryStudents(String fileName) throws IOException {
        file = new File(fileName);
        if(!file.exists())
            file.createNewFile();
        readFromFile();
    }

    public void add(Student student) throws RepositoryError {
        if(exists(student))
            throw new RepositoryError("Element existent!");
        entities.add(student);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Student st1, Student st2) throws RepositoryError {
        readFromFile();
        super.update(st1,st2);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void delete(Student student) throws RepositoryError {
        readFromFile();
        super.delete(student);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(Student student){
        readFromFile();
        return super.exists(student);

    }

    public List<Student> getAll(){
        readFromFile();
        return super.getAll();
    }

}
