package com.github.nilstrieb.cofig;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class Config {

    public static final String PREFIX = "$";
    public static final Color DEFAULT_COLOR = new Color(255, 255, 255);
    public static final long THIS_ID = 801015254023798825L;
    private static JDA jda;

    public static void setJda(JDA jda) {
        Config.jda = jda;
    }

    public static EmbedBuilder getDefaultEmbed() {
        User thisBot = jda.getUserById(THIS_ID);
        if (thisBot == null) {
            thisBot = jda.retrieveUserById(THIS_ID).complete();
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Config.DEFAULT_COLOR).
                setThumbnail(thisBot.getAvatarUrl())
                .setFooter("bot");
        return builder;
    }
}
