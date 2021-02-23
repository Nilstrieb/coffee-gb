package com.github.nilstrieb.emulator;

import eu.rekawek.coffeegb.gpu.Display;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiscordDisplay implements Display {

    private final TextChannel channel;
    private final Message message;


    public static final int DISPLAY_WIDTH = 160;
    public static final int DISPLAY_HEIGHT = 144;

    public static final int[] COLORS = new int[]{0xe6f8da, 0x99c886, 0x437969, 0x051f2a};

    private BufferedImage img;

    private final int[] rgb;
    private int i;

    private int frameCounter = 0;

    private AtomicBoolean sending = new AtomicBoolean(false);


    public DiscordDisplay(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Pokemon Red");

        this.channel = channel;
        this.message = channel.sendMessage(builder.build()).complete();

        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        rgb = new int[DISPLAY_WIDTH * DISPLAY_HEIGHT];
        img = gfxConfig.createCompatibleImage(DISPLAY_WIDTH, DISPLAY_HEIGHT);
    }

    @Override
    public void putDmgPixel(int color) {
        rgb[i++] = COLORS[color];
        i = i % rgb.length;
    }

    @Override
    public void putColorPixel(int gbcRgb) {
        rgb[i++] = translateGbcRgb(gbcRgb);
    }

    @Override
    public void requestRefresh() {
        i = 0;
        img.setRGB(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, rgb, 0, DISPLAY_WIDTH);
        frameCounter++;

        if (frameCounter % 60 == 0) {
            Runnable runnable = () -> {
                sending.set(true);
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    ImageIO.write(img, "png", bytes);
                    bytes.flush();

                    String frameUrl = message.getJDA().getGuildById(459006129670979584L)
                            .getTextChannelById(813778460988014612L)
                            .sendFile(bytes.toByteArray(), "img.png")
                            .complete()
                            .getAttachments()
                            .get(0)
                            .getUrl();

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("Pokemon Red")
                            .setFooter("Frame " + frameCounter)
                            .setImage(frameUrl);

                    message.editMessage(builder.build()).queue();

                    //channel.sendMessage(builder.build()).addFile(bytes.toByteArray(), "img.png").queue(m -> System.out.println("sent img"));

                    bytes.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            CompletableFuture.runAsync(runnable).thenRun(() -> sending.set(false));
        }
    }

    @Override
    public synchronized void waitForRefresh() {
        while (sending.get()) {
            try {
                wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void enableLcd() {
    }

    @Override
    public void disableLcd() {
    }

    public static int translateGbcRgb(int gbcRgb) {
        int r = (gbcRgb >> 0) & 0x1f;
        int g = (gbcRgb >> 5) & 0x1f;
        int b = (gbcRgb >> 10) & 0x1f;
        int result = (r * 8) << 16;
        result |= (g * 8) << 8;
        result |= (b * 8) << 0;
        return result;
    }
}
