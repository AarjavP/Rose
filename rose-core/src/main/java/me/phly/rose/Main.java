package me.phly.rose;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        jdaBuilder.setToken("");
        JDA jda;
        try {
            jda = jdaBuilder.buildAsync();
        } catch (LoginException | RateLimitedException e) {
            e.printStackTrace();
            return;
        }
        jda.addEventListener(new SimpleListener());

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
