package congressbot.discord;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class UpcomingBillEmbed implements Embeddable {

    private String source;
    private String date;
    private LinkedList<String> items;

    public UpcomingBillEmbed(String source, String date) {
        this.source = source;
        this.date = date;
        this.items = new LinkedList<String>();
        this.items.add("");
    }

    public void appendBillItem(String bill) {
        if (items.peekLast().length() + bill.length() < 1024) {
            String s = items.pollLast() + bill;
            items.addLast(s);
        } else {
            items.addLast(bill);
        }
    }

    @Override
    public Color getDiscordEmbedColor() {
        return Color.LIGHT_GRAY;
    }

    @Override
    public String getDiscordEmbedTitle() {
        return "Upcoming legislation for the " + source;
    }

    @Override
    public String getDiscordEmbedTitleUrl() {
        return null;
    }

    @Override
    public String getDiscordEmbedDescription() {
        return String.format("Below are bills and resolutions noted as having upcoming action as of %s. " +
                "This list is typically updated on a weekly basis. " +
                "For each entry, clicking the corresponding hyperlink will take you directly to its text.", date);
    }

    @Override
    public String getDiscordEmbedFooter() {
        return "To request further information on any of this upcoming legislation, just enter the corresponding !bill command";
    }

    public MessageEmbed.AuthorInfo getDiscordEmbedAuthor() {
        return new MessageEmbed.AuthorInfo("Generic Congress Info",
                null,
                "https://case.house.gov/sites/case.house.gov/files/styles/congress_featured_image/public/featured_image/us-capitol.jpg?itok=N24dyGse",
                null);
    }

    public List<MessageEmbed.Field> getDiscordEmbedFields(boolean isExpanded) {
        java.util.List<MessageEmbed.Field> fields = new LinkedList<MessageEmbed.Field>();
        for (String s : items) {
            fields.add(new MessageEmbed.Field("\u200b", s, false));
        }
        return fields;
    }

}
