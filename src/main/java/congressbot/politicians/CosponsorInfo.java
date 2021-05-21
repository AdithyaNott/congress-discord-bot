package congressbot.politicians;

public class CosponsorInfo {
    private final int democratCosponsors;
    private final int republicanCosponsors;
    private final int independentCosponsors;
    private final int totalCosponsors;
    private final String cosponsorsString;

    public CosponsorInfo(int democratCosponsors, int republicanCosponsors, int independentCosponsors) {
        this.democratCosponsors = democratCosponsors;
        this.republicanCosponsors = republicanCosponsors;
        this.independentCosponsors = independentCosponsors;
        this.totalCosponsors = democratCosponsors + republicanCosponsors + independentCosponsors;

        StringBuilder cosponsors = new StringBuilder();
        if (democratCosponsors > 0) {
            cosponsors.append(" :regional_indicator_d: ");
            cosponsors.append(democratCosponsors);
        }
        if (republicanCosponsors > 0) {
            cosponsors.append(" :regional_indicator_r: ");
            cosponsors.append(republicanCosponsors);
        }
        if (independentCosponsors > 0) {
            cosponsors.append(" :regional_indicator_i: ");
            cosponsors.append(independentCosponsors);
        }
        if (cosponsors.length() == 0) {
            cosponsors.append("0");
        }
        this.cosponsorsString = cosponsors.toString();
    }

    @Override
    public String toString() {
        return cosponsorsString;
    }

    public int getNumCosponsors() {
        return totalCosponsors;
    }

    public int getNumDemocratCosponsors() {
        return democratCosponsors;
    }

    public int getNumRepublicanCosponsors() {
        return republicanCosponsors;
    }

    public int getNumIndependentCosponsors() {
        return independentCosponsors;
    }
}