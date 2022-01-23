package listener;

import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class BotListener implements EventListener {

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
//            String msg = ((MessageReceivedEvent) event).getMessage().getContentRaw();
        }
    }

}
