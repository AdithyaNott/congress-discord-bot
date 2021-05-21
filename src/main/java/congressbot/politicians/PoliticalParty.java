package congressbot.politicians;

public enum PoliticalParty {
    DEMOCRAT("D"), REPUBLICAN("R"), INDEPENDENT("I");

    private final String label;

    PoliticalParty(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return label;
    }

    public static PoliticalParty stringToPoliticalParty(String s) {
        s = s.toLowerCase();
        PoliticalParty party = null;
        switch (s) {
            case "d":
                party = PoliticalParty.DEMOCRAT;
                break;
            case "r":
                party = PoliticalParty.REPUBLICAN;
                break;
            case "i":
            case "id":
                party = PoliticalParty.INDEPENDENT;
                break;
        }
        return party;
    }
}