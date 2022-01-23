package bot;

import listener.BotListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class BreachTrackerBot {

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault("token")
                .addEventListeners(new BotListener())
                .setActivity(Activity.playing("Type !ping"));
        builder.build();
    }

}
