package com.katehdiffo.parts;

import ro.pippo.core.Pippo;

public class AppRunner {

    public static void main(String[] args) {
        final PartValidator partValidator = new PartValidator();
        final InMemoryPartRepository partRepository = new InMemoryPartRepository();
        Pippo pippo = new Pippo(
                new PartApplication(
                        partRepository,
                        new PartService(
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