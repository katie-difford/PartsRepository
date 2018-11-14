package com.katehdiffo.parts;

import ro.pippo.core.Pippo;

import java.util.ArrayList;

public class AppRunner {

    public static void main(String[] args) {
        Pippo pippo = new Pippo(new PartApplication(new ArrayList<>()));
        pippo.getServer().setPort(2211);
        pippo.start();
        System.out.println("Running yeh");
    }

}