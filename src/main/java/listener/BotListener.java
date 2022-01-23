package listener;

import httpClient.Client;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import persistence.Asset;
import persistence.Paste;

import java.util.*;

public class BotListener implements EventListener {
    private String prefix = "-";
    private HashMap<String, ArrayList<User>> tracking_map = new HashMap<>();
    private HashMap<String, TextChannel> trackingChannels = new HashMap<>();
    private int days = 5;
    private double min_interval = 0.5;
//    private TextChannel channel;
    private String key;
    private Date prevBreachCheck;
    private TimerTask checkBreaches;
    private long unit_multiplier = 1000L;
    private Timer timer = new Timer();

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
            prevBreachCheck = Calendar.getInstance().getTime();
            checkBreaches = checkBreaches();
            timer.scheduleAtFixedRate(checkBreaches, 0, days * unit_multiplier);
        }

        if (event instanceof ShutdownEvent) {
            System.out.println("***** BOT IS OFFLINE *****");
        }

        if (event instanceof MessageReceivedEvent) {
            commandRouter((MessageReceivedEvent) event);
        }
        if (event instanceof GuildJoinEvent){

            Guild guild = ((GuildJoinEvent) event).getGuild();
            trackingChannels.put(guild.getId(), guild.getTextChannels().get(0));
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
            case "-removetrack":
                removeTrackCommand(message, messageParts);
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
                prefix + "removeTrack\n" +
                "\t Usage: " + prefix + "removetrack {the email or account name you want to track} \n" +
                "\t Description: no longer notifies you if the account name or email is in a recent breach after each breach check \n" +
                "\t Example: " + prefix + "removetrack email testemail@gmail.com  \n"+
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
        trackingChannels.put(message.getGuild().getId(), textChannels.get(0));
        message.reply("Tracking information will now be set to the channel #"+ messageParts[1]).queue();

    }

    private void removeTrackCommand(Message message, String[] messageParts){
        if(messageParts.length != 2){
            sendDM(message.getAuthor(), "Incorrect usage of " + prefix + "removetrack \n\t\tUsage: " + prefix + "removetrack {the email or account name you no longer want to track}");
            message.delete().queue();
            return;
        }

        ArrayList<User> users;
        if (tracking_map.containsKey(messageParts[1])) {
            users = tracking_map.get(messageParts[1]);
        }else{
            sendDM(message.getAuthor(), messageParts[1] + " wasn't being tracked");
            message.delete().queue();
            return;
        }
        if (!users.contains(message.getAuthor())){
            sendDM(message.getAuthor(), messageParts[1] + " wasn't being tracked");
        }else{
            users.remove(message.getAuthor());
            sendDM(message.getAuthor(), messageParts[1] + " is no longer being tracked");
        }


        message.delete().queue();

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

    private void DMTrackedAccounts(){
        String[] accounts =  tracking_map.keySet().toArray(new String[tracking_map.size()]);
        for(int i = 0; i < accounts.length; i++){
            Paste email;
            Asset account;
            String dm;
            try {
                email = Client.fetchContent(accounts[i], this.key);
                account = Client.fetchPaste(accounts[i], this.key);
            } catch (Exception e) {
                return;
            }

            if (email == null) {
                dm = "No new breaches have occurred on the email \"" + accounts[i] + "\" since " + prevBreachCheck.toString();
            } else {
                dm = "There have been breaches on your email, change your passwords.";
            }

            if (account == null) {
                dm += "\nNo new breaches have occurred on the email \"" + accounts[i] + "\" since " + prevBreachCheck.toString();
            } else {
                dm += "There have been breaches on your email, change your passwords.";
            }

            ArrayList<User> userList = tracking_map.get(accounts[i]);
            for(int j = 0; j < userList.size(); j++){
                sendDM(userList.get(j), dm);
            }
        }
    }

    private TimerTask checkBreaches(){
        return new TimerTask() {
            @Override
            public void run() {

                DMTrackedAccounts();
                sendTrackingMessageToChannels();
                prevBreachCheck = Calendar.getInstance().getTime();
            }
        };
    }


    private void sendTrackingMessageToChannels(){
        String[] guildIds = trackingChannels.keySet().toArray(new String[trackingChannels.size()]);
        for(int i = 0; i < guildIds.length; i++){
            trackingChannels.get(guildIds[i]).sendMessage("No new Breaches since " + prevBreachCheck.toString()).queue();
        }
    }
}
