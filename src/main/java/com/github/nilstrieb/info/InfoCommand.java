package com.github.nilstrieb.info;

import com.github.nilstrieb.cofig.Config;
import com.github.nilstrieb.core.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;

public class InfoCommand extends Command {
    private static final String INVITE_LINK =
            "(<link>)";

    public InfoCommand() {
        super("info", "Get the invite link for this bot");
    }

    @Override
    public void called(String args) {

        EmbedBuilder builder = Config.getDefaultEmbed()
                .setTitle("Invite Killua to your server!")
                .addField("Invite link", "[Invite]" + INVITE_LINK, true)
                .setFooter("This bot was made by myself");
        reply(builder.build());
    }
}
