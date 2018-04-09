package com.company.Domain;

import java.io.Serializable;

public class Student implements Serializable,Comparable<Student> {

    private int idStudent;
    private String nume;
    private int grupa;
    private String email;
    private String profesorIndrumator;

    /**
     * Creeaza un constructor cu:
     * @param idStudent
     * @param nume
     * @param grupa
     * @param email
     * @param profesorIndrumator
     */

    public Student(int idStudent, String nume, int grupa, String email, String profesorIndrumator) {
        this.idStudent = idStudent;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.profesorIndrumator = profesorIndrumator;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public String getProfesorIndrumator() {
        return profesorIndrumator;
    }

    public void setProfesorIndrumator(String profesorIndrumator) {
        this.profesorIndrumator = profesorIndrumator;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getGrupa() {
        return grupa;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return idStudent + "|" + nume + "|" + grupa +"|"+ email + "|" + profesorIndrumator+ "|";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return idStudent == student.idStudent;
    }

    @Override
    public int compareTo(Student student2){
        return ((Student)this).getNume().compareTo(((Student)student2).getNume());
    }


}
