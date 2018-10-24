package com.katehdiffo.parts;

import ro.pippo.core.Pippo;

public class AppRunner {

    public static void main(String[] args) {
        Pippo pippo = new Pippo(new PartApplication());
        pippo.getServer().setPort(2211);
        pippo.start();
    }

}