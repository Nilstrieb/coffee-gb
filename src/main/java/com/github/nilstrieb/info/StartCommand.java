package com.github.nilstrieb.info;

import com.github.nilstrieb.core.command.Command;
import com.github.nilstrieb.emulator.DiscordController;
import com.github.nilstrieb.emulator.DiscordDisplay;
import eu.rekawek.coffeegb.gui.EmulatorMain;

public class StartCommand extends Command {

    public StartCommand() {
        super("start", "Start the game");
    }

    @Override
    public void called(String args) {
        Runnable runnable = () -> {
            try {
                System.out.println("Starting...");
                EmulatorMain.start(new DiscordDisplay(event.getTextChannel()),
                        new DiscordController(event.getTextChannel().getIdLong(), event.getAuthor().getIdLong()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(runnable).start();
    }
}
