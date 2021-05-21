package congressbot.dataretrieval;

import congressbot.discord.CongressBillEmbed;
import congressbot.discord.Embeddable;
import congressbot.legislation.CongressBill;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public abstract class CongressDataManager {

    public abstract CongressBill createCongressBill(String billType, int congressSession, int billNum);

    protected Message createCongressBillDiscordMessage(String billType, int congressSession, int billNum, boolean isExpanded) {
        MessageBuilder mb = new MessageBuilder();
        try {
            CongressBill cb = createCongressBill(billType, congressSession, billNum);
            CongressBillEmbed cbEmbed = new CongressBillEmbed(cb);
            MessageEmbed embed = (isExpanded) ? Embeddable.createMessageEmbed(cbEmbed) : Embeddable.createAbbreviatedMessageEmbed(cbEmbed);
            mb.setEmbed(embed);
        } catch (Exception e) {
            mb.append(String.format("No records found for bill %s%d-%d", billType, billNum, congressSession));
        }
        return mb.build();
    }

    public Message createCongressBillMessage(String billType, int congressSession, int billNum) {
        return createCongressBillDiscordMessage(billType, congressSession, billNum, false);
    }

    public abstract Message createUpcomingSenateBillDiscordMessages();

    public abstract Message createUpcomingHouseBillDiscordMessages();

//    msg, status = congress_api.get_upcoming_house_requests()
//    msg = msg[0]
//    await message.channel.send("There are {0} upcoming bills for the House as of {1}. They will be listed below.".format(len(msg["bills"]), msg["date"]))
//    for bill in msg["bills"]:
//        msg, embed = bill_message(bill["congress"], bill["bill_slug"])
//        if embed is not None:
//            await message.channel.send(embed=embed)
//    public CongressBill[] getUpcomingSenateBills();

//    public CongressBill[] getUpcomingHouseBills();
}