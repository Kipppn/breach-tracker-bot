package listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class BotListener implements EventListener {
    String prefix = "-";
    HashMap<String, ArrayList<User>> tracking_map = new HashMap<>();

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

    /**
     * This function determines which command the user has entered
     * @param event the event representing the user sending a message
     */
    private void commandRouter(MessageReceivedEvent event){
        Message message = event.getMessage();
        String messageText = message.getContentRaw();
        String[] messageParts = messageText.split(" ");
        if(!messageText.substring(0, 1).equals(prefix)) {
            return;
        }

        switch (messageParts[0].toLowerCase()){
            case "-help":
                helpCommand(message);
                break;
            case "-track":
                track(message, messageParts);
                break;

        }



    }

    /**
     * This function handles the help command
     * @param message the message sent by the user
     */
    private void helpCommand(Message message){
        message.reply("Commands:\n" +
                prefix + "help\n" +
                "\t Usage:" + prefix + "help\n" +
                "\t Description: Gives a list of commands \n" +
                prefix + "track\n" +
                "\t Usage: " + prefix + "track {the email or account name you want to track}\n" +
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

    /**
     * This function handles the track command
     *
     * @param message a string representing the message sent by the user
     * @param messageParts a string array representing the message sent by the user split up by spaces
     */
    private void track(Message message, String[] messageParts){
        if(messageParts.length != 2){
            sendDM(message.getAuthor(), "Incorrect usage of " + prefix + "track \n\t\tUsage: " + prefix + "track {the email or account name you want to track}");
            message.delete();
            return;
        }

        ArrayList<User> users = new ArrayList<>();
        if (tracking_map.containsKey(messageParts[1])) {
            users = tracking_map.get(messageParts[1]);
        }
        if (!users.contains(message.getAuthor())){
            users.add(message.getAuthor());
            tracking_map.put(messageParts[1], users);
            sendDM(message.getAuthor(), messageParts[1] +" will now be tracked ");
        }else{
            sendDM(message.getAuthor(), messageParts[1] + " is already being tracked");
        }


        message.delete();

    }

    /**
     * This function sends a dm to a specified user
     *
     * @param user The user to send a DM to
     * @param message The message you will send to the user
     */
    private void sendDM(User user, String message){
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }


}
