package com.company.Validation;

import com.company.Domain.Nota;

import java.util.List;
import java.util.Vector;

public class NotaValidator implements Validator<Nota> {


    private String getAllErrors(List<String> erori){

        String eroareValidare = new String();
        for(String eroare : erori)
            eroareValidare = eroareValidare.concat(eroare);
        return eroareValidare;

    }

    @Override
    public void validate(Nota nota) throws ValidationException {

        Vector<String> listaErori = new Vector<>();
        if(nota.getIdStudent()<=0)
            listaErori.add("\tId Student invalid\n");
        if(nota.getIdTema()<=0)
            listaErori.add("\tId Tema invalid\n");
        if(nota.getNota()<1 || nota.getNota()>10)
            listaErori.add("\tNota invalida\n");
        if(listaErori.isEmpty())
            return;

        throw new ValidationException(getAllErrors(listaErori));

    }

}
