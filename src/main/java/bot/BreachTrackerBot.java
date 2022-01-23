package bot;

import listener.BotListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BreachTrackerBot {

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new BotListener())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("searching for breaches"));
        builder.build();
    }

}
