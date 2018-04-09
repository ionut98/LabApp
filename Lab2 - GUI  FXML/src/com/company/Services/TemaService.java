package com.company.Services;

import com.company.Domain.TemaLaborator;
import com.company.Errors.RepositoryError;
import com.company.Repository.AbstractRepository;
import com.company.Utils.*;
import com.company.Validation.TemaValidator;
import com.company.Validation.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class TemaService implements Service<TemaLaborator>,Observable<TemaLaborator>{

    private AbstractRepository <TemaLaborator> repository;
    private TemaValidator temaValidator;
    ArrayList<Observer<TemaLaborator>> temeObservers = new ArrayList<>();

    public TemaService(AbstractRepository<TemaLaborator> repository, TemaValidator temaValidator) {
        this.repository = repository;
        this.temaValidator = temaValidator;
    }

    public void updateDeadline(int nrTema, int newDeadline, int saptCurenta) throws RepositoryError {

        for(TemaLaborator tema : repository.getAll()) {
            if (tema.getNrTema() == nrTema ) {
                if (tema.getDeadline() < saptCurenta)
                    throw new RepositoryError("Nu se mai pot face schimbari! Este prea tarziu!");
                else if (newDeadline <= tema.getDeadline() || newDeadline > 14)
                    throw new RepositoryError("Deadline invalid!!!");
                else {
                    repository.update(tema, new TemaLaborator(nrTema, newDeadline, tema.getCerinta()));
                    ListEvent<TemaLaborator> event = createEvent(ListEventType.UPDATE,tema,repository.getAll());
                    notifyObservers(event);
                }
            }
            }
        }


    @Override
    public int size() {
        return repository.size();
    }

    @Override
    public void add(TemaLaborator tema) throws RepositoryError, ValidationException {
        temaValidator.validate(tema);
        repository.add(tema);
        ListEvent<TemaLaborator> event = createEvent(ListEventType.ADD,null,repository.getAll());
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

    @Override
    public void update(TemaLaborator entity1, TemaLaborator entity2) throws RepositoryError, ValidationException {
        return;
    }

    @Override
    public void delete(TemaLaborator tema) throws RepositoryError {
        repository.delete(tema);
    }

    @Override
    public boolean exists(TemaLaborator tema) {
        return repository.exists(tema);
    }

    public List<TemaLaborator> getAll(){
        return repository.getAll();
    }

    public boolean exists(int idTema){
        for(TemaLaborator tema : repository.getAll())
            if(tema.getNrTema() == idTema)
                return true;
        return false;
    }

    public int getDeadLine(int nrTema){
        for (TemaLaborator tema: repository.getAll())
            if(tema.getNrTema() == nrTema)
                return tema.getDeadline();
        return -1;
    }

    public List<TemaLaborator> filtrareCerinta(String cerinta){
        List<TemaLaborator> teme = repository.getAll();
        return Filter.filterAndSorter(teme, tema->tema.getCerinta().toLowerCase().contains(cerinta.toLowerCase()),(TemaLaborator tema1, TemaLaborator tema2)->{
            return tema1.compareTo(tema2);
        });
    }

    public List<TemaLaborator> filtrareDeadline(int deadline){
        List<TemaLaborator> teme = repository.getAll();
        return Filter.filterAndSorter(teme, tema->tema.getDeadline()<deadline,(TemaLaborator tema1, TemaLaborator tema2)->{
            return tema1.compareTo(tema2);
        });
    }


    public List<TemaLaborator> filtrareCerintaDeadline(String cerinta, int deadline){
        List<TemaLaborator> teme = repository.getAll();
        return Filter.filterAndSorter(teme, tema->(tema.getDeadline()<deadline && tema.getCerinta().toLowerCase().contains(cerinta.toLowerCase())),(TemaLaborator tema1, TemaLaborator tema2)->{
            return tema1.compareTo(tema2);
        });
    }

    public TemaLaborator getById(int nrTema){

        for(TemaLaborator tema: repository.getAll())
            if(tema.getNrTema()==nrTema)
                return tema;
        return null;
    }

    @Override
    public void addObserver(Observer<TemaLaborator> o) {
        temeObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<TemaLaborator> o) {
        temeObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<TemaLaborator> event) {
        temeObservers.forEach(e->e.notifyEvent(event));
    }
}
