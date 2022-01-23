package listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotListener implements EventListener {
    private String prefix = "-";
    private HashMap<String, ArrayList<User>> tracking_map = new HashMap<>();
    private double days = 1;
    private double min_interval = 0.5;
    private TextChannel channel;
    private String key;

    /**
     * User passes in API Key; Quick and Dirty, not a fan, but for a demo, it's good to go!
     * @param key
     */
    public BotListener(String key) {
        this.key = key;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) {
            System.out.println("***** BOT IS READY *****");
        }

        if (event instanceof ShutdownEvent) {
            System.out.println("***** BOT IS OFFLINE *****");
        }

        if (event instanceof MessageReceivedEvent) {
            commandRouter((MessageReceivedEvent) event);
        }
    }

    /**
     * This function determines which command the user has entered
     * @param event the event representing the user sending a message
     */
    private void commandRouter(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String messageText = message.getContentRaw();
        String[] messageParts = messageText.split(" ");
        if(!messageText.substring(0, 1).equals(prefix)) {
            return;
        }
      
        switch (messageParts[0].toLowerCase()) {
            case "-ping":
                pingCommand(message);
                break;
            case "-help":
                helpCommand(message);
                break;
            case "-track":
                trackCommand(message, messageParts);
                break;
            case "-setinterval":
                setIntervalCommand(message, messageParts);
                break;
            case "-setchannel":
                setChannel(message, messageParts);
                break;
        }
    }
  
    /**
     * Will return the message 'Pong' as a sign that the bot is up-and-running!
     * @param message message sent by the Discord user
     */
    private void pingCommand(Message message) {
        message.reply("Pong").queue();
    }

    /**
     * This function handles the help command
     * @param message the message sent by the user
     */
    private void helpCommand(Message message) {
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
     * @param message the message sent by the user
     * @param messageParts a string array representing the message sent by the user split up by spaces
     */
    private void trackCommand(Message message, String[] messageParts) {
        if(messageParts.length != 2){
            sendDM(message.getAuthor(), "Incorrect usage of " + prefix + "track \n\t\tUsage: " + prefix + "track {the email or account name you want to track}");
            message.delete().queue();
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


        message.delete().queue();

    }
    /**
     * This function handles the setchannel command
     *
     * @param message the message sent by the user
     * @param messageParts a string array representing the message sent by the user split up by spaces
     */
    private void setChannel(Message message, String[] messageParts) {
        if(messageParts.length != 2){
            message.reply("Incorrect usage of " + prefix + "setchannel \n\t\tUsage: " + prefix + "setchannel {the text channel which is sent breach info}\n");
        }else if (!message.getMember().hasPermission(Permission.ADMINISTRATOR)){
            message.reply("To use this command, you must be an admin").queue();
            return;
        }
        List<TextChannel> textChannels = message.getGuild().getTextChannelsByName(messageParts[1], true);
        if(textChannels.size() < 1){
            message.reply("No text channels with the name " + messageParts[1]).queue();
            return;
        }else if(textChannels.size() > 1){
            message.reply("Multiple text channels with the name " + messageParts[1] + " please rename this text channel or select another one.").queue();
            return;
        }
        channel = textChannels.get(0);
        message.reply(messageParts[1] + " channel set" ).queue();

    }
    /**
     * This function handles the setinterval command
     *
     * @param message the message sent by the user
     * @param messageParts a string array representing the message sent by the user split up by spaces
     */
    private void setIntervalCommand(Message message, String[] messageParts) {
        // TODO: Flip this IF-ELSEIF, We should check for permissions first before anything else is checked.
        if(messageParts.length != 2 ){
            message.reply("Incorrect Usage of " + prefix + "setinterval \n\t\tUsage: -setinterval {number of days}").queue();
            return;
        } else if (!message.getMember().hasPermission(Permission.ADMINISTRATOR)){
            message.reply("To use this command, you must be an admin").queue();
            return;
        }

        double interval;
        try {
             interval = Double.parseDouble(messageParts[1]);
        }catch (NumberFormatException e){
            message.reply(messageParts[1] + " is not an number").queue();
            return;
        }
        if(interval >= min_interval){
            days = interval;
            message.reply("Interval is now set to " + interval + " days").queue();
        }else {
            message.reply("Interval is shorter than 0.5 days which is too short").queue();
        }
    }

    /**
     * This function sends a dm to a specified user
     *
     * @param user The user to send a DM to
     * @param message The message you will send to the user
     */
    private void sendDM(User user, String message) {
        user.openPrivateChannel().flatMap(channel -> channel.sendMessage(message)).queue();
    }
}
