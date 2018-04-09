package com.company.Repository;

import com.company.Domain.Nota;
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

public class FileRepositoryGrades extends RepositoryInMemory<Nota> {


    File file;

    private void readFromFile(){
        entities.clear();
        String relativePath = file.getName();
        Path path = Paths.get(relativePath);
        Stream<String> lines;
        try {
            lines= Files.lines(path);
            lines.forEach((t)->{
                String fields[] = t.split(";");
                int idStudent=Integer.parseInt(fields[0]);
                int nrTema = Integer.parseInt(fields[1]);
                float valoare = Float.parseFloat(fields[2]);
                entities.add(new Nota(idStudent,nrTema,valoare));
            });
        } catch (IOException e) {
            System.out.println("");
        }

    }

    private void writeToFile() throws IOException {
        String relativePath = file.getName();
        Path path = Paths.get(relativePath);
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {

            entities.forEach(n -> {
                try {
                    bufferedWriter.write(n.getIdStudent()+";"+n.getIdTema()+";"+n.getNota()+"\n");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    public FileRepositoryGrades(String fileName) throws IOException {
        file = new File(fileName);
        if(!file.exists())
            file.createNewFile();
        readFromFile();
    }

    public void add(Nota nota) throws RepositoryError {
        if(exists(nota))
            throw new RepositoryError("Element existent!");
        entities.add(nota);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Nota n1, Nota n2) throws RepositoryError {
        readFromFile();
        super.update(n1,n2);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void delete(Nota nota) throws RepositoryError {
        readFromFile();
        super.delete(nota);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(Nota nota){
        readFromFile();
        return super.exists(nota);

    }

    public List<Nota> getAll(){
        readFromFile();
        return super.getAll();
    }



}
