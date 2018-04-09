package com.company.Services;

import com.company.Domain.Student;
import com.company.Errors.RepositoryError;
import com.company.Repository.AbstractRepository;
import com.company.Utils.*;
import com.company.Validation.StudentValidator;
import com.company.Validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService implements Service<Student>,Observable<Student> {

    private AbstractRepository <Student> repository;
    private StudentValidator studentValidator;
    ArrayList<Observer<Student>> studentsObservers=new ArrayList<>();

    public Student getStudent(int id){

        for(Student stud : repository.getAll())
            if(stud.getIdStudent() == id)
                return stud;
        return null;

    }

    public String getEmail(int id){
        for (Student st: repository.getAll())
            if(st.getIdStudent()==id)
                return st.getEmail();
        return null;
    }

    public StudentService(AbstractRepository<Student> repository, StudentValidator studentValidator) {
        this.repository = repository;
        this.studentValidator = studentValidator;
    }

    public void add(Student stud) throws RepositoryError, ValidationException {
        studentValidator.validate(stud);
        repository.add(stud);
        ListEvent<Student> event = createEvent(ListEventType.ADD,null,repository.getAll());
        notifyObservers(event);
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

    public void update(Student stud1, Student stud2) throws RepositoryError, ValidationException {

        if (getStudent(stud1.getIdStudent()) != null) {
            studentValidator.validate(stud2);
            repository.update(stud1, stud2);
            ListEvent<Student> event = createEvent(ListEventType.UPDATE, stud1,repository.getAll());
            notifyObservers(event);

        }
    }
    public void delete(Student student) throws RepositoryError{

        repository.delete(student);
        ListEvent<Student> event = createEvent(ListEventType.REMOVE,student,repository.getAll());
        notifyObservers(event);

    }

    @Override
    public int size() {
        return repository.size();
    }

    @Override
    public boolean exists(Student student) {
        return repository.exists(student);
    }

    public List<Student> getAll(){
        return repository.getAll();
    }

    public boolean exists(int id){
        for(Student st : repository.getAll())
            if(st.getIdStudent() == id)
                return true;
        return false;
    }

    public List<Student> filtrareGrupa(int grupa){
        List<Student> studenti = repository.getAll();
        return Filter.filterAndSorter(studenti, st->st.getGrupa()==grupa,(Student st1, Student st2)->{
            return st1.compareTo(st2);
        });
    }

    public List<Student> filtrareDomeniuEmail(String domain){
        List<Student> studenti = repository.getAll();
        return Filter.filterAndSorter(studenti, st->st.getEmail().contains(domain),(Student st1, Student st2)->{
            return st1.compareTo(st2);
        });
    }

    public List<Student> filtrareProfesor(String profesor){
        List<Student> studenti = repository.getAll();
        return Filter.filterAndSorter(studenti, st->st.getProfesorIndrumator().equals(profesor),(Student st1, Student st2)->{
            return st1.compareTo(st2);
        });
    }

    @Override
    public void addObserver(Observer<Student> o) {
        studentsObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<Student> o) {
        studentsObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        studentsObservers.forEach(x->x.notifyEvent(event));
    }
}
