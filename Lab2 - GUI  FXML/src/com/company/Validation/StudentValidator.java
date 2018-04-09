package com.company.Validation;
import com.company.Domain.Student;

import java.util.List;
import java.util.Vector;

public class StudentValidator implements Validator<Student> {

    private String getAllErrors(List<String> erori){

        String eroareValidare = new String();
        for(String eroare : erori)
            eroareValidare = eroareValidare.concat(eroare);

        return eroareValidare;
    }

    @Override
    public void validate(Student student) throws ValidationException {

        Vector<String> listaErori = new Vector<>();
        if(student.getIdStudent()<=0)
            listaErori.add("\tId invalid\n");
        if(student.getNume().isEmpty())
            listaErori.add("\tNume invalid\n");
        if(student.getGrupa()<=0)
            listaErori.add("\tGrupa invalida\n");
        if(student.getEmail().isEmpty())
            listaErori.add("\tEmail invalid\n");
        if(student.getProfesorIndrumator().isEmpty())
            listaErori.add("\tNume profesor invalid\n");

        if(listaErori.isEmpty())
            return;

        throw new ValidationException(getAllErrors(listaErori));


    }
}
