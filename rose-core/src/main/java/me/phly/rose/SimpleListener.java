package me.phly.rose;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleListener extends ListenerAdapter {

//    private static final Pattern spaceSplitPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
    private Map<String, String> parrot = new LinkedHashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        JDA jda = event.getJDA();
//        List<Emote> emotes = jda.getEmotes();

        Message message = event.getMessage();
        String strippedContent = message.getStrippedContent();
        if ("!ping".equals(strippedContent)) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("pong").queue();
        } else if ("!kek".equals(strippedContent)) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage(jda.getEmoteById(378045797415124994L).getAsMention()).queue();
        } else if (strippedContent != null) {
            if (strippedContent.startsWith( "!" )) {
                String[] split = strippedContent.split( " ", 3 );
                if ( split.length == 3  && "!parrot".equals( split[0] )) {
                    parrot.put( split[ 1 ], split[ 2 ] );
                    event.getChannel().sendMessage( "got it" ).queue();
                    return;
                } else if ( split.length == 2 && "!rp".equals( split[0] )) {
                    parrot.remove( split[1] );
                    event.getChannel().sendMessage( "K" ).queue();
                    return;
                }
            }
            if (parrot.containsKey( strippedContent )) {
                event.getChannel().sendMessage( parrot.get( strippedContent ) ).queue();
            }
        }
    }
}
