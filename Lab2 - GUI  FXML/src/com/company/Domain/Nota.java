package com.company.Domain;

import java.io.Serializable;
import java.util.Comparator;

public class Nota implements Serializable,Comparable<Nota> {

    private int idStudent;
    private int idTema;
    private float nota;

    public Nota(int idStudent, int idTema, float nota) {

        this.idStudent = idStudent;
        this.idTema = idTema;
        this.nota = nota;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public int getIdTema() {
        return idTema;
    }

    public void setIdTema(int idTema) {
        this.idTema = idTema;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nota nota1 = (Nota) o;

        return idStudent == nota1.idStudent && idTema == nota1.idTema;
    }

    @Override
    public String toString() {
        return idStudent +"|"+ idTema +"|"+ nota;
    }


    @Override
    public int compareTo(Nota nota) {
        return -(int) (((Nota)this).getNota()-((Nota)nota).getNota());
    }
}
