package com.github.nilstrieb.core;

import com.github.nilstrieb.cofig.Config;
import com.github.nilstrieb.cofig.Secrets;
import com.github.nilstrieb.core.command.CommandListener;
import com.github.nilstrieb.core.reactions.ReactionEventListener;
import com.github.nilstrieb.core.sections.ChannelMessageListener;
import com.github.nilstrieb.info.HelpCommand;
import com.github.nilstrieb.info.InfoCommand;
import com.github.nilstrieb.info.StartCommand;
import com.github.nilstrieb.listener.StartUpListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class BotMain {

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(Secrets.TOKEN);
        builder.setCompression(Compression.ZLIB)
                .setActivity(Activity.watching("over Gon"))
                .disableIntents(GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_EMOJIS,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGE_TYPING)
                .disableCache(CacheFlag.EMOTE, CacheFlag.VOICE_STATE);


        builder.addEventListeners(
                new StartUpListener(),
                new ChannelMessageListener(),
                new CommandListener(),
                new ReactionEventListener()
        );

        JDA jda = builder.build();
        setupCommands();
        Config.setJda(jda);
    }

    private static void setupCommands() {
        new HelpCommand();
        new InfoCommand();
        new StartCommand();
    }
}
