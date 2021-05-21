package congressbot.dataretrieval;

import congressbot.discord.Embeddable;
import congressbot.discord.UpcomingBillEmbed;
import congressbot.legislation.CongressBill;
import congressbot.legislation.HouseActionable;
import congressbot.legislation.SenateActionable;
import congressbot.legislation.Vetoable;
import congressbot.politicians.CongressSponsor;
import congressbot.politicians.CosponsorInfo;
import congressbot.politicians.PoliticalParty;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PropublicaDataManager extends CongressDataManager {

    private final HttpClient client;
    //FIXME integrate API_KEY better
    private static final String API_KEY = null;

    public PropublicaDataManager() {
        this.client = HttpClient.newBuilder().build();
    }

    @Override
    public Message createUpcomingHouseBillDiscordMessages() {
        return createUpcomingBillsHelper(true);
    }

    @Override
    public Message createUpcomingSenateBillDiscordMessages() {
        return createUpcomingBillsHelper(false);
    }

    private Message createUpcomingBillsHelper(boolean isHouse) {
        Message message;
        String source = (isHouse) ? "house" : "senate";
        String sourceFormal = (isHouse) ? "House of Representatives" : "Senate";
        try {
            JSONObject billJSON = executeRequest(String.format("/bills/upcoming/%s.json", source)).getJSONArray("results").getJSONObject(0);
            JSONArray bills = billJSON.getJSONArray("bills");

            UpcomingBillEmbed upcomingBills = new UpcomingBillEmbed(sourceFormal, billJSON.getString("date"));

            for (int i = 0; i < bills.length(); i++) {
                JSONObject bill = bills.getJSONObject(i);
                String billLabel = bill.getString("bill_number");
                String billUrl = bill.getString("bill_url");
                String billName = bill.getString("description");

                if (billLabel.isEmpty() || billUrl.isEmpty() || billName.isEmpty()) {
                    JSONObject cJSON = executeDirectURLRequest(bill.getString("api_uri")).getJSONArray("results").getJSONObject(0);
                    billLabel = cJSON.getString("number");
                    billUrl = cJSON.getString("congressdotgov_url");
                    billName = cJSON.getString("short_title");
                }
                upcomingBills.appendBillItem(String.format("* [%s](%s): %s\n", billLabel, billUrl, billName));
            }
            message = new MessageBuilder(Embeddable.createAbbreviatedMessageEmbed(upcomingBills)).build();
        } catch (Exception e) {
            message = new MessageBuilder(String.format("Unable to fetch upcoming %s legislation", sourceFormal)).build();
        }
        return message;
    }

    public JSONObject executeRequest(String endpoint) throws IOException, InterruptedException {
        return executeDirectURLRequest("https://api.propublica.org/congress/v1/" + endpoint);
    }

    private JSONObject executeDirectURLRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("X-API-KEY", API_KEY)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }

    private CongressBill initCongressBillFromJSONObject(JSONObject billJSON) {

        String billType = billJSON.getString("bill_type");
        String billUrl = billJSON.getString("congressdotgov_url");
        String shortTitle = String.format("%s: %s",
                billJSON.getString("number"),
                billJSON.getString("short_title"));
        String longTitle = billJSON.getString("title");

        String summary = billJSON.getString("summary");

        String sponsorId = billJSON.getString("sponsor_id");
        String sponsorTitle = billJSON.getString("sponsor_title");
        String sponsorBaseName = billJSON.getString("sponsor");
        PoliticalParty sponsorParty = PoliticalParty.stringToPoliticalParty(billJSON.getString("sponsor_party"));
        String sponsorState = billJSON.getString("sponsor_state");

        CongressSponsor sponsor = new CongressSponsor(sponsorId, sponsorBaseName, sponsorTitle, sponsorState, sponsorParty);

        JSONObject cosponsorsByParty = billJSON.getJSONObject("cosponsors_by_party");
        int democratCosponsors = (cosponsorsByParty.has("D")) ? cosponsorsByParty.getInt("D") : 0;
        int republicanCosponsors = (cosponsorsByParty.has("R")) ? cosponsorsByParty.getInt("R") : 0;
        int independentCosponsors = (cosponsorsByParty.has("ID")) ? cosponsorsByParty.getInt("ID") : 0;
        CosponsorInfo cosponsorInfo = new CosponsorInfo(democratCosponsors, republicanCosponsors, independentCosponsors);

        CongressBill congressBill = CongressBill.CongressBillBuilder.newInstance(billType)
                .billUrl(billUrl)
                .longTitle(longTitle)
                .sponsors(sponsor, cosponsorInfo)
                .shortTitle(shortTitle)
                .summary(summary)
                .build();
        return congressBill;
    }

    public CongressBill createCongressBill(String billType, int congressSession, int billNum) {
        JSONObject billJSON;
        CongressBill congressBill;
        try {
            String endpoint = String.format("/%d/bills/%s%d.json", congressSession, billType, billNum);
            billJSON = executeRequest(endpoint).getJSONArray("results").getJSONObject(0);
            ;

            congressBill = initCongressBillFromJSONObject(billJSON);
        } catch (Exception e) {
            return null;
        }

        if (congressBill instanceof HouseActionable) {
            String date = (!billJSON.isNull("house_passage")) ? billJSON.getString("house_passage") : null;
            if (date != null) {
                ((HouseActionable) congressBill).recordHouseVote(date != null, propublicaDateStrToDate(date));
            }
        }
        if (congressBill instanceof SenateActionable) {
            //FIXME NULL DOES NOT AUTOMATICALLY MEAN NO PASS
            String date = (!billJSON.isNull("senate_passage")) ? billJSON.getString("senate_passage") : null;
            if (date != null) {
                ((SenateActionable) congressBill).recordSenateVote(date != null, propublicaDateStrToDate(date));
            }
        }

        if (congressBill instanceof Vetoable) {
            String vetoDate = (!billJSON.isNull("vetoed")) ? billJSON.getString("vetoed") : null;
            String enactedDate = !billJSON.isNull("enacted") ? billJSON.getString("enacted") : null;
            if (vetoDate != null) {
                ((Vetoable) congressBill).recordPresidentAction(false, propublicaDateStrToDate(vetoDate));
            } else if (enactedDate != null) {
                ((Vetoable) congressBill).recordPresidentAction(true, propublicaDateStrToDate(enactedDate));
            }
        }

        return congressBill;
    }

    private static Date propublicaDateStrToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
