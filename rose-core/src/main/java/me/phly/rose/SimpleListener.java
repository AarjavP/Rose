package me.phly.rose;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import me.phly.rose.db.TriggersStore;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SimpleListener extends ListenerAdapter {

    public final TriggersStore triggersStore;

    private final ScheduledExecutorService executorService;
    private       MessageChannel           lastChannel;
    private       List<Future<?>>          futures;
    private String wowee = "<:wowee:391845750360899594>";
    private Multimap<Long, Long> incognitos;

    public SimpleListener() throws IOException {
        triggersStore = new TriggersStore();
        executorService = Executors.newScheduledThreadPool( 1 );
        futures = new ArrayList<>();
        incognitos = LinkedListMultimap.create();
    }

    public void wowee() {
        if ( lastChannel != null ) {
            lastChannel.sendMessage( wowee ).queue();
        }
    }

    public void clearFutures() {
        List<Future<?>> saved = this.futures;
        futures = new ArrayList<>();
        for ( Future<?> future : saved ) {
            future.cancel( false );
        }
    }

    private void doNothing(Object ignored) {}

    private Consumer<Message> record(Message command) {
        long channelId = command.getChannel().getIdLong();
        if (incognitos.containsKey( channelId )) {
            incognitos.put( channelId, command.getIdLong() );
            return msg -> incognitos.put( msg.getChannel().getIdLong(), msg.getIdLong() );
        }
        return this::doNothing;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        MessageChannel channel = event.getChannel();
        lastChannel = channel;
        clearFutures();
        futures.add( executorService.schedule( this::wowee, 4, TimeUnit.HOURS ) );

        Message message = event.getMessage();
        String content = message.getContentDisplay();

        if ("ping".equals(content)) {
            channel.sendMessage("pong").queue(record( message ));
        }

        if ("kek".equals(content)) {
            channel.sendMessage("<:FeelsKekMan:378045797415124994>").queue(record( message ));
        }

        if ("help".equals( content )) {
            channel.sendMessage( "is just 1 p away from hell").queue(record( message ));
            String help = "I'm jk\n" + "To add triggers: `yo, <trigger> | <response>`\n" +
                          "To list triggers: `triggers`, To remove a trigger: `forget <trigger>`";
            channel.sendMessage( help ).queueAfter( 2, TimeUnit.SECONDS );
        }

        if (content == null) return;

        String lc = content.trim().toLowerCase();

        if (lc.startsWith( "say " ) && lc.length() > 4) {
            channel.sendMessage( message.getContentRaw().substring( 4 ) ).queue(record( message ));
        }

        if (lc.startsWith( "yo, " ) && lc.length() > 4) {
            String[] args = content.substring( 4 ).split( "(?<!\\\\)\\|", 2 );
            String trigger = "", response = "";
            if (args.length == 2) {
                trigger= args[0].trim();
                response = message.getContentRaw().substring( 4 ).split( "(?<!\\\\)\\|", 2 )[1];
            }
            if (!trigger.isEmpty() && !response.isEmpty()) {
                triggersStore.add( trigger, response );
                channel.sendMessage( trigger + " triggers me now" ).queue(record( message ));
            }

        } else if (lc.startsWith( "forget " ) && lc.length() > 7) {
            String toForget = lc.substring( 7 ).trim();
            if (triggersStore.has( toForget )) {
                triggersStore.remove( toForget );
                channel.sendMessage( "I forgot about " + toForget ).queue(record( message ));
            }
        } else if (lc.equals( "triggers" )) {
            Set<String>   triggers = triggersStore.getTriggers();
            String list;
            if (triggers.size() == 0) {
                list = "Nothing triggers me yet";
            } else {
                list = triggers.stream().map( it -> it.replace( "|", "\\|" ) )
                               .collect( Collectors.joining(" | ") );
            }
            channel.sendMessage( list ).queue(record( message ));
        } else if (lc.equals( "incognito" )) {
            if ( incognitos.containsKey( channel.getIdLong() ) ) {
                channel.sendMessage( "Already in incognito mode" ).queue(record( message ));
            } else {
                incognitos.put( channel.getIdLong(), message.getIdLong() );
            }
        } else if (lc.equals( "kleanup" )) {
            kleanup( ( TextChannel ) channel );
        } else if (triggersStore.has( lc )) {
            channel.sendMessage( triggersStore.getResponse( lc ) ).queue(record( message ));
        }
    }

    private void kleanup(TextChannel channel) {
        Collection<Long> toDelete  = incognitos.removeAll( channel.getIdLong() );
        if (toDelete.size() > 1) {
            channel.deleteMessagesByIds(toDelete.stream().map( Long::toUnsignedString )
                                                .collect( Collectors.toList() )  ).queue();
        } else if (toDelete.size() == 1) {
            channel.deleteMessageById( toDelete.iterator().next() ).queue();
        }
    }


    @Override
    public void onShutdown( ShutdownEvent event ) {
        System.out.println("Shutting down");
        try {
            triggersStore.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        executorService.shutdown();

        try {
            executorService.awaitTermination( 5, TimeUnit.SECONDS );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        } finally {
            executorService.shutdownNow();
        }
    }
}
