package com.app.sygen.services.serviceDeliberation;

import java.util.Comparator;

public class compareByUe implements Comparator<TableDeliberation>{

    @Override
    public int compare(TableDeliberation arg0, TableDeliberation arg1) {
        int result = arg0.getCodeUe().compareTo(arg1.getCodeUe());
        int bool;
        if(result < 0){
            bool = -1;
        }
        else{
            bool = 1;
        }
        return bool;
    }

}
