package com.company.Repository;

import com.company.Domain.TemaLaborator;
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

public class FileRepositoryLabs extends RepositoryInMemory<TemaLaborator> {


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
                int nrTema=Integer.parseInt(fields[0]);
                int deadline = Integer.parseInt(fields[1]);
                String cerinta = fields[2];
                entities.add(new TemaLaborator(nrTema,deadline,cerinta));
            });
        } catch (IOException e) {
            System.out.println("");
        }

    }

    private void writeToFile() throws IOException {
        String relativePath = file.getName();
        Path path = Paths.get(relativePath);
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING)) {

            entities.forEach(t -> {
                try {
                    bufferedWriter.write(t.getNrTema()+";"+t.getDeadline()+";"+t.getCerinta()+"\n");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
    }

    public FileRepositoryLabs(String fileName) throws IOException {
        file = new File(fileName);
        if(!file.exists())
            file.createNewFile();
        readFromFile();
    }

    public void add(TemaLaborator temaLaborator) throws RepositoryError {
        if(exists(temaLaborator))
            throw new RepositoryError("Element existent!");
        entities.add(temaLaborator);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(TemaLaborator t1, TemaLaborator t2) throws RepositoryError {
        readFromFile();
        super.update(t1,t2);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void delete(TemaLaborator temaLaborator) throws RepositoryError {
        readFromFile();
        super.delete(temaLaborator);
        try {
            writeToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(TemaLaborator temaLaborator){
        readFromFile();
        return super.exists(temaLaborator);

    }

    public List<TemaLaborator> getAll(){
        readFromFile();
        return super.getAll();
    }


}
