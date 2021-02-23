package com.github.nilstrieb.emulator;

import com.github.nilstrieb.core.sections.ChannelListener;
import com.github.nilstrieb.core.sections.ChannelMessageEventManager;
import eu.rekawek.coffeegb.controller.ButtonListener;
import eu.rekawek.coffeegb.controller.ButtonListener.Button;
import eu.rekawek.coffeegb.controller.Controller;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class DiscordController implements Controller, ChannelListener {

    private final long channelID;
    private final long userID;
    private ButtonListener listener;

    private Map<String, Button> inputMap;

    public DiscordController(long channelID, long userID) {
        ChannelMessageEventManager.addListener(this, channelID);
        this.channelID = channelID;
        this.userID = userID;

        this.inputMap = new HashMap<>();
        inputMap.put("a", Button.LEFT);
        inputMap.put("d", Button.RIGHT);
        inputMap.put("w", Button.UP);
        inputMap.put("s", Button.DOWN);
        inputMap.put("q", Button.A);
        inputMap.put("e", Button.B);
        inputMap.put("f", Button.START);
        inputMap.put("g", Button.SELECT);
    }

    @Override
    public void setButtonListener(ButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public void messageReceived(MessageReceivedEvent event) {
        String text = event.getMessage().getContentRaw().trim();

        if (inputMap.containsKey(text)) {
            Button button = inputMap.get(text);
            listener.onButtonPress(button);
            System.out.println("Press: " + button);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listener.onButtonRelease(button);
        } else {
            event.getTextChannel().sendMessage("Unkown key").queue(msg -> msg.delete().queueAfter(1, TimeUnit.SECONDS));
        }

        event.getMessage().delete().queue();
    }

    @Override
    public long getUserID() {
        return userID;
    }

    @Override
    public long getChannelID() {
        return channelID;
    }
}
