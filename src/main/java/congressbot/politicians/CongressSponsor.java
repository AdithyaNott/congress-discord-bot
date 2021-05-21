package congressbot.politicians;

public class CongressSponsor {

    private final String id;
    private final String name;
    private final PoliticalParty politicalParty;
    private final String title;
    private final String state;

    public CongressSponsor(String id, String name, String title, String state, PoliticalParty politicalParty) {
        this.id = id;
        this.name = name;
        this.politicalParty = politicalParty;
        this.title = title;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public String getFormalName() {
        return String.format("%s %s (%s-%s)", title, name, politicalParty.getLabel(), state);
    }
}