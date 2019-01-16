package com.katehdiffo.parts;

import com.katehdiffo.parts.application.InMemoryPartRepository;
import com.katehdiffo.parts.application.PartService;
import com.katehdiffo.parts.application.PartValidator;
import com.katehdiffo.parts.web.PartApplication;
import ro.pippo.core.Pippo;

public class AppRunner {

    private static final int DEFAULT_PORT = 2211;

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
        pippo.getServer().setPort(DEFAULT_PORT);
        pippo.start();

        System.out.println("Running on port " + DEFAULT_PORT + "... yeh");
    }
}