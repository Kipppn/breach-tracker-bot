package listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class BotListener implements EventListener {
    String prefix = "!";
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            System.out.println("***** BOT IS READY *****");
        }

        if (event instanceof ShutdownEvent) {
            System.out.println("***** BOT IS OFFLINE *****");
        }

        if (event instanceof MessageReceivedEvent) {
            // TODO: Implement PING command (for health check of bot)
            commandRouter((MessageReceivedEvent) event);
//            String msg = ((MessageReceivedEvent) event).getMessage().getContentRaw();
        }
    }

    public void commandRouter(MessageReceivedEvent event){
        Message message = event.getMessage();
        String messageText = message.getContentRaw();
        if(!messageText.substring(0, 1).equals(prefix)){
            return;
        }

        switch (messageText.toLowerCase()){
            case "!help":

                message.reply("Commands:\n" +
                        prefix + "help\n" +
                            "\t Usage:" + prefix + "help\n" +
                            "\t Description: Gives a list of commands \n" +
                        prefix + "track\n" +
                            "\t Usage: " + prefix + "track [email or accountname] {the email or account name you want to track}\n" +
                            "\t Description: notifies you if the account name or email is in a recent breach after each breach check\n" +
                            "\t Example: " + prefix + "track email testemail@gmail.com \n"+
                        prefix + "setinterval\n" +
                            "\t Usage: " + prefix + "setinterval {number of days} \n" +
                            "\t Description: sets the amount of days it waits to checks for breaches \n" +
                            "\t Example: " + prefix + "setinterval 5 \n"+
                        prefix + "setchannel\n" +
                            "\t Usage: " + prefix + "setchannel {the text channel which is sent breach info}\n" +
                            "\t Description: sets the channel that the bot will send breach information to \n" +
                            "\t Example: " + prefix + "setchannel #general \n").queue();
        }



    }

}
