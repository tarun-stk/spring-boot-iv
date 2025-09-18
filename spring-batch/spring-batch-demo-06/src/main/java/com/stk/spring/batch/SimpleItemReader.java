package com.stk.spring.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleItemReader implements ItemReader<String> {

    List<String> dataset = new ArrayList<>();
    Iterator<String> iterator;

    public SimpleItemReader() {
        dataset.add("1");
        dataset.add("2");
        dataset.add("3");
        dataset.add("4");
        dataset.add("5");
        iterator = dataset.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
