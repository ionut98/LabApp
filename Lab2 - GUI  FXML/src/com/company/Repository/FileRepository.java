package com.company.Repository;

import com.company.Errors.RepositoryError;

import java.io.*;
import java.util.List;

public class FileRepository<E> extends RepositoryInMemory<E>  {

        private File file;

        private void readFromFile(){
            entities.clear();

            try(ObjectInputStream fin = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file.getName())))) {
                E entity;
                while((entity = (E)fin.readObject()) != null)entities.add(entity);

            } catch (FileNotFoundException e) {
                System.out.println("Fisier neidentificat la citire!!");
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
                System.out.println("Clasa neidentificata!!");
            }

        }

    private void writeToFile(){

            try(ObjectOutputStream fout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file.getName(),false)))) {
                for(E entity: entities) fout.writeObject(entity);
                } catch (Exception e1) {
                System.out.println("Eroare writeToFile!");
            }

    }

    public FileRepository(String fileName) throws IOException {
        file = new File(fileName);
        if(!file.exists())
            file.createNewFile();
        readFromFile();
    }

    public void add(E entity) throws RepositoryError {
        if(exists(entity))
            throw new RepositoryError("Element existent!");
        entities.add(entity);
        writeToFile();
    }

    public void update(E entity1, E entity2) throws RepositoryError {
        readFromFile();
            super.update(entity1,entity2);
        writeToFile();

    }

    public void delete(E entity) throws RepositoryError {
        readFromFile();
            super.delete(entity);
        writeToFile();
    }

    public boolean exists(E entity){
        readFromFile();
            return super.exists(entity);

    }

    public List<E> getAll(){
        readFromFile();
        return super.getAll();
    }

}
