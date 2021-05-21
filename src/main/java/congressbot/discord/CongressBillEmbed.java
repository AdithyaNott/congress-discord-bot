package congressbot.discord;

import congressbot.legislation.CongressBill;
import congressbot.politicians.PoliticalParty;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

public final class CongressBillEmbed implements Embeddable {
    private final CongressBill congressBill;

    private final Color sponsorsColor;

    public CongressBillEmbed(CongressBill congressBill) {
        this.congressBill = congressBill;


        int numSponsors = 1 + congressBill.getCosponsorInfo().getNumCosponsors();

        EnumMap<PoliticalParty, Integer> partyMap = new EnumMap<PoliticalParty, Integer>(PoliticalParty.class);

        partyMap.put(PoliticalParty.DEMOCRAT,
                congressBill.getCosponsorInfo().getNumDemocratCosponsors());
        partyMap.put(PoliticalParty.REPUBLICAN,
                congressBill.getCosponsorInfo().getNumRepublicanCosponsors());
        partyMap.put(PoliticalParty.INDEPENDENT,
                congressBill.getCosponsorInfo().getNumIndependentCosponsors());

        partyMap.put(congressBill.getSponsor().getPoliticalParty(),
                partyMap.get(congressBill.getSponsor().getPoliticalParty()) + 1);

        this.sponsorsColor = new Color(255 * partyMap.get(PoliticalParty.REPUBLICAN) / numSponsors,
                255 * partyMap.get(PoliticalParty.INDEPENDENT) / numSponsors,
                255 * partyMap.get(PoliticalParty.DEMOCRAT) / numSponsors);

    }

    @Override
    public Color getDiscordEmbedColor() {
        return sponsorsColor;
    }

    @Override
    public String getDiscordEmbedTitle() {
        return congressBill.getShortTitle();
    }

    @Override
    public String getDiscordEmbedTitleUrl() {
        return congressBill.getBillUrl();
    }

    @Override
    public String getDiscordEmbedDescription() {
        return congressBill.getLongTitle();
    }

    @Override
    public String getDiscordEmbedFooter() {
        return congressBill.getExplanation();
    }

    @Override
    public MessageEmbed.AuthorInfo getDiscordEmbedAuthor() {
        return new MessageEmbed.AuthorInfo(congressBill.getSponsor().getFormalName(),
                null,
                String.format("https://raw.githubusercontent.com/unitedstates/images/gh-pages/congress/225x275/%s.jpg", congressBill.getSponsor().getId()),
                null);
    }

    private String generateLegislativeChecklist() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < congressBill.getLegislativeStepsLength(); i++) {
            if (congressBill.getLegislativeStep(i) == null) {
                break;
            }

            if (congressBill.getLegislativeStep(i).isPassed() == null) {
                sb.append(":white_square_button: ");
            } else if (congressBill.getLegislativeStep(i).isPassed()) {
                sb.append(":white_check_mark: ");
            } else {
                sb.append(":x: ");
            }

            sb.append(congressBill.getLegislativeStep(i).getDescription());

            if (congressBill.getLegislativeStep(i).getDate() != null) {
                sb.append(" (");
                sb.append(new SimpleDateFormat("M/dd/yy").format(congressBill.getLegislativeStep(i).getDate()).toString());
                sb.append(")");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<MessageEmbed.Field> getDiscordEmbedFields(boolean isExpanded) {
        List<MessageEmbed.Field> fields = new LinkedList<MessageEmbed.Field>();
        fields.add(new MessageEmbed.Field("Legislative Checklist", generateLegislativeChecklist(), true));
        fields.add(new MessageEmbed.Field("Cosponsors", congressBill.getCosponsorInfo().toString(), true));
        if (isExpanded) {
            fields.add(new MessageEmbed.Field("Text Summary", congressBill.getSummary(), false));
        }
        return fields;
    }
}
