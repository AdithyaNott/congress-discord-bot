package congressbot.discord;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.List;

public interface Embeddable {
    public Color getDiscordEmbedColor();

    public String getDiscordEmbedTitle();

    public String getDiscordEmbedTitleUrl();

    public String getDiscordEmbedDescription();

    public String getDiscordEmbedFooter();

    public MessageEmbed.AuthorInfo getDiscordEmbedAuthor();

    public List<MessageEmbed.Field> getDiscordEmbedFields(boolean isExpanded);

    public static MessageEmbed createAbbreviatedMessageEmbed(Embeddable embeddable) {
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(embeddable.getDiscordEmbedTitle(), embeddable.getDiscordEmbedTitleUrl())
                .setColor(embeddable.getDiscordEmbedColor())
                .setAuthor(embeddable.getDiscordEmbedAuthor().getName(),
                        null, embeddable.getDiscordEmbedAuthor().getIconUrl())
                .setDescription(embeddable.getDiscordEmbedDescription())
                .setFooter("In the future you can react with \uD83D\uDCDC to expand! But not yet...\n" +
                        "In the meantime, just go to the link if you want the full text.");
        for (MessageEmbed.Field field : embeddable.getDiscordEmbedFields(false)) {
            eb.addField(field);
        }
        return eb.build();
    }

    public static MessageEmbed createMessageEmbed(Embeddable embeddable) {
        EmbedBuilder eb = new EmbedBuilder()
                .setTitle(embeddable.getDiscordEmbedTitle(), embeddable.getDiscordEmbedTitleUrl())
                .setColor(embeddable.getDiscordEmbedColor())
                .setFooter(embeddable.getDiscordEmbedFooter())
                .setAuthor(embeddable.getDiscordEmbedAuthor().getName(),
                        null, embeddable.getDiscordEmbedAuthor().getIconUrl())
                .setDescription(embeddable.getDiscordEmbedDescription());
        for (MessageEmbed.Field field : embeddable.getDiscordEmbedFields(true)) {
            eb.addField(field);
        }
        return eb.build();
    }
}
