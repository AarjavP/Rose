package me.phly.rose;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        Properties props = new Properties();
        File roseHome = new File( System.getProperty( "user.home" ), "rose" );
        roseHome.mkdirs();
        File propsFile = new File( roseHome, "rose.properties" );
        try ( Reader in = new BufferedReader( new FileReader( propsFile ) ) ) {
            props.load( in );
        } catch ( IOException e ) {
            e.printStackTrace();
            return;
        }
        jdaBuilder.setToken(props.getProperty( "token", "" ));
        JDA jda;
        try {
            jda = jdaBuilder.buildAsync();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }
        try {
            jda.addEventListener(new SimpleListener());
        } catch ( IOException e ) {
            e.printStackTrace();
            return;
        }

        System.out.println("waiting");
        try {
            int read = System.in.read();
            System.out.println("read " + read);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jda.shutdown();
    }

}
