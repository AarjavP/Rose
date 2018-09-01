package me.phly.rose.db;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TriggersStore implements AutoCloseable {
    private static final File TRIGGERS_FILE;

    //    public static final String CONNECTION_URL;
    static {
        File roseHome = new File( System.getProperty( "user.home" ), "rose" );
        roseHome.mkdirs();
        TRIGGERS_FILE = new File( roseHome, "triggers.yml" );
//        CONNECTION_URL = "jdbc:sqlite:" + triggersStoreFile.getAbsolutePath();
    }

//    private final Connection connection;

    private final Map<String, String> triggerToResponse;

    public static class TriggerEntry {
        public String trigger;
        public String response;

        public TriggerEntry() {}

        public TriggerEntry( String trigger, String response ) {
            this.trigger = trigger;
            this.response = response;
        }
    }

    private static class TriggersYML {
        public List<TriggerEntry> triggers;
    }

    public TriggersStore() throws IOException {
//        connection = DriverManager.getConnection( CONNECTION_URL );
        triggerToResponse = new ConcurrentHashMap<>();
        if ( TRIGGERS_FILE.exists() ) {
            Yaml        yml = new Yaml();
            TriggersYML triggers;
            try ( InputStream in = new BufferedInputStream( new FileInputStream( TRIGGERS_FILE ) ) ) {
                triggers = yml.loadAs( in, TriggersYML.class );
            }
            triggers.triggers.forEach( t -> add( t.trigger, t.response ) );
            System.out.println("Loaded " + triggers.triggers.size() + " triggers from " + TRIGGERS_FILE);
        }
    }

    public Set<String> getTriggers() {
        return triggerToResponse.keySet();
    }

    public boolean has(String trigger) {
        return triggerToResponse.containsKey( trigger );
    }

    public String getResponse(String forTrigger) {
        return triggerToResponse.get( forTrigger );
    }

    public void add(String trigger, String response) {
        triggerToResponse.put( trigger.toLowerCase(), response );
    }

    public void remove(String trigger) {
        triggerToResponse.remove( trigger );
    }

    @Override
    public void close() throws IOException {
        TriggersYML triggersYML = new TriggersYML();
        triggersYML.triggers = new LinkedList<>();
        for ( Entry<String, String> entry : triggerToResponse.entrySet() ) {
            triggersYML.triggers.add( new TriggerEntry( entry.getKey(), entry.getValue() ) );
        }
        Yaml yml = new Yaml();
        try ( BufferedWriter out = new BufferedWriter( new FileWriter( TRIGGERS_FILE, false ) ) ) {
            yml.dump( triggersYML, out );
        }
        System.out.println("Dumped " + triggersYML.triggers.size() + " triggers to " + TRIGGERS_FILE);
    }
}
