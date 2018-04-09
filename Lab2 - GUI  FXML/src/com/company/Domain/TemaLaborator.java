package com.company.Domain;

import java.io.Serializable;

public class TemaLaborator implements Serializable,Comparable<TemaLaborator>{

    private int nrTema,deadline;
    private String cerinta;

    public TemaLaborator(int nrTema, int deadline, String cerinta) {
        this.nrTema = nrTema;
        this.deadline = deadline;
        this.cerinta = cerinta;
    }

    public int getNrTema() {
        return nrTema;
    }

    public void setNrTema(int nrTema) {
        this.nrTema = nrTema;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getCerinta() {
        return cerinta;
    }

    public void setCerinta(String cerinta) {
        this.cerinta = cerinta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemaLaborator that = (TemaLaborator) o;

        return nrTema == that.nrTema;
    }

    @Override
    public String toString() {
        return nrTema + "|"+ deadline + "|"+ cerinta;
    }

    @Override
    public int compareTo(TemaLaborator temaLaborator) {
        return ((TemaLaborator)this).getDeadline()-((TemaLaborator)temaLaborator).getDeadline();
    }
}
