package eu.rekawek.coffeegb.gui;

import eu.rekawek.coffeegb.controller.Controller;
import eu.rekawek.coffeegb.gpu.Display;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EmulatorMain {

    public static void main(String[] args) throws Exception {
        String[] fixedArgs = {"game.gb"};
        System.setProperty("apple.awt.application.name", "Coffee GB");
        new Emulator(fixedArgs, loadProperties(), null, null).run();
    }

    public static void start(Display d, Controller c) throws Exception {
        System.setProperty("apple.awt.application.name", "Coffee GB");
        new Emulator(new String[]{"game.gb"}, loadProperties(), d, c).run();
    }

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        File propFile = new File(new File(System.getProperty("user.home")), ".coffeegb.properties");
        if (propFile.exists()) {
            try (FileReader reader = new FileReader(propFile)) {
                props.load(reader);
            }
        }
        return props;
    }

}
