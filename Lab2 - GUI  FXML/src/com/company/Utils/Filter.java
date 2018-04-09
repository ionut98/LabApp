package com.company.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Filter {

    public static  <E> List<E> filterAndSorter(List<E> list, Predicate<E> predicate,Comparator<E> comparator){

        List<E> filtered = list.stream().filter(predicate).sorted(comparator).collect(toList());
        return filtered;
    }
}
