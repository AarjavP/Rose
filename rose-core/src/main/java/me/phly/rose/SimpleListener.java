package me.phly.rose;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

public class SimpleListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        JDA jda = event.getJDA();
        List<Emote> emotes = jda.getEmotes();

        Message message = event.getMessage();
        String strippedContent = message.getStrippedContent();
        if ("!ping".equals(strippedContent)) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("pong").queue();
        }

        if ("!kek".equals(strippedContent)) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage(jda.getEmoteById(378045797415124994L).getAsMention()).queue();
        }

    }
}
