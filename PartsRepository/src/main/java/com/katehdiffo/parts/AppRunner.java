package com.katehdiffo.parts;

import ro.pippo.core.Pippo;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.stream.LongStream.iterate;

public class AppRunner {

    public static void main(String[] args) {
        final PartValidator partValidator = new PartValidator();
        final InMemoryPartRepository partRepository = new InMemoryPartRepository();
        Pippo pippo = new Pippo(
                new PartApplication(
                        partRepository,
                        new CreatePartService(
                                partRepository,
                                partValidator
                        )
                )
        );
        pippo.getServer().setPort(2211);
        pippo.start();
        System.out.println("Running yeh");
    }
}