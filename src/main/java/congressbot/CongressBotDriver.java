package congressbot;

import congressbot.dataretrieval.CongressDataManager;
import congressbot.dataretrieval.PropublicaDataManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CongressBotDriver extends ListenerAdapter {
    private final CongressDataManager dataManager;

    public CongressBotDriver() {
        this.dataManager = new PropublicaDataManager();
    }

    public static void main(String[] args) throws LoginException {
        //FIXME integrate discord token better
        JDABuilder builder = JDABuilder.createDefault(null);
        builder.addEventListeners(new CongressBotDriver()).setActivity(Activity.listening("webmin!help")).build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String command = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        if (command.startsWith("!bill")) {
            handleBillCommand(channel, command);
        } else if (command.startsWith("!house")) {
            handleUpcomingHouseBillsCommand(channel);
        } else if (command.startsWith("!senate")) {
            handleUpcomingSenateBillsCommand(channel);
        } else if (command.trim().equals("webmin!help")) {
            String help_message = "I accept commands in both channels and DMs. This version supports the following commands: \n" +
                    " * !bill type - lists available congressional bill types to query\n" +
                    " * !bill type## - Retrieves corresponding bill information for current congress (e.g. !bill hr1 retrieves H.R. 1 from the current session) \n" +

                    " * !bill type##-## - Retrieves corresponding bill information for specified Congress " +
                    "(e.g. !bill s1-116 retrieves S. 1 from the 116th session)\n" +
                    " * !house - Retrieves information on upcoming bills being presented in the House of Representatives\n" +
                    " * !senate - Retrieves information on upcoming bills being presented in the Senate\n" +
                    " * webmin!help - Posts this message";
            sendMessageToChannel(channel, help_message);
        }
    }

    private void handleUpcomingHouseBillsCommand(MessageChannel channel) {
        sendMessageToChannel(channel, dataManager.createUpcomingHouseBillDiscordMessages());
    }

    private void handleUpcomingSenateBillsCommand(MessageChannel channel) {
        sendMessageToChannel(channel, dataManager.createUpcomingSenateBillDiscordMessages());
    }

    //TODO let's just deal with channel rather than full event...
    private void handleBillCommand(MessageChannel channel, String command) {
        command = command.toLowerCase().replace(".", "").trim();
        Pattern pattern = Pattern.compile("^!bill\\s*([^0-9]+)\\s*(\\d+)(?:\\s*-\\s*(\\d+))?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);

        Pattern typeHelpPattern = Pattern.compile("^!bill\\s*type\\s*$");
        if (matcher.find()) {
            String billType = matcher.group(1);
            int billNum = Integer.parseInt(matcher.group(2));
            //TODO we can probably dynamically determine which session we're on TBH. Always starts January 3 every 2 years since 20th amendment.
            int congressSession = (matcher.group(3) != null) ? Integer.parseInt(matcher.group(3)) : 117;

            sendMessageToChannel(channel, dataManager.createCongressBillMessage(billType, congressSession, billNum));
        } else {
            String helpMessage;
            if (typeHelpPattern.matcher(command).find()) {
                helpMessage = "The following bill types are supported:\n" +
                        "* hr - House Bill\n" +
                        "* s - Senate Bill\n" +
                        "* hres - House Resolution\n" +
                        "* sres - Senate Resolution\n" +
                        "* hjres - House Joint Resolution\n" +
                        "* sjres - Senate Joint Resolution\n" +
                        "* hconres - House Concurrent Resolution\n" +
                        "* sconres - Senate Concurrent Resolution\n" +
                        "To find H.R.1, you can enter `!bill hr1`. To find H.R.1 from the 116th session you can enter `!bill hr1-116`";
            } else {
                helpMessage = "Could not process input.\n" +
                        "Make sure you are entering it in the format !bill billtype##, such as `!bill hres1`\n" +
                        "Alternatively, you can enter !bill billtype##-### to specify a previous congress, " +
                        "such as `!bill hr6395-116` to pull up the NDAA for FY 2021";
            }
            sendMessageToChannel(channel, helpMessage);
        }
    }

    private void sendMessageToChannel(MessageChannel channel, String message) {
        channel.sendTyping().queue();
        channel.sendMessage(message).queue();
    }

    private void sendMessageToChannel(MessageChannel channel, Message message) {
        channel.sendTyping().queue();
        channel.sendMessage(message).queue();
    }

}
