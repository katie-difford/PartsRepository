package com.katehdiffo.parts;

import ro.pippo.core.Pippo;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.stream.LongStream.iterate;

public class AppRunner {

    private static final Iterator<Long> ids = iterate(1, i -> i + 1).iterator();

    public static void main(String[] args) {
        Pippo pippo = new Pippo(new PartApplication(new ArrayList<>(), () -> ids.next(), new PartValidator()));
        pippo.getServer().setPort(2211);
        pippo.start();
        System.out.println("Running yeh");
    }
}