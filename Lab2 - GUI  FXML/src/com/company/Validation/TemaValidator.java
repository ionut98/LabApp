package com.company.Validation;

import com.company.Domain.TemaLaborator;
import java.util.List;
import java.util.Vector;

public class TemaValidator implements Validator<TemaLaborator> {


    private String getAllErrors(List<String> erori){

        String eroareValidare = new String();
        for(String eroare : erori)
            eroareValidare = eroareValidare.concat(eroare);

        return eroareValidare;
    }

    @Override
    public void validate(TemaLaborator temaLaborator) throws ValidationException {

        Vector<String> listaErori = new Vector<>();
        if(temaLaborator.getNrTema()<=0)
            listaErori.add("\tNumar tema invalid\n");
        if(temaLaborator.getCerinta().isEmpty())
            listaErori.add("\tLipsa cerinta\n");
        if(temaLaborator.getDeadline()<1 && temaLaborator.getDeadline()>14)
            listaErori.add("\tDeadline invalid\n");

        if(listaErori.isEmpty())
            return;

        throw new ValidationException(getAllErrors(listaErori));

    }
}
