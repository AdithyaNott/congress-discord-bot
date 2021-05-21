package congressbot.legislation;

import java.util.Date;

//FIXME CHANGE TO CLASS
public class LegislativeStep {

    public static LegislativeStep newHouseVote() {
        return new LegislativeStep("Approval by House of Representatives");
    }

    public static LegislativeStep newSenateVote() {
        return new LegislativeStep("Approval by Senate");
    }

    public static LegislativeStep newPresidentAction() {
        return new LegislativeStep("Approval by President");
    }

    public static LegislativeStep newHouseOverride() {
        return new LegislativeStep("2/3 Veto Override by House of Representatives (currently not tracked)");
    }

    public static LegislativeStep newSenateOverride() {
        return new LegislativeStep("2/3 Veto Override by Senate (currently not tracked)");
    }

    private Boolean isPassed;
    private Date date;
    private String description;

    protected LegislativeStep(String description) {
        this.description = description;
        this.date = null;
    }

    public void setStatus(boolean isPassed, Date date) {
        this.isPassed = (Boolean) isPassed;
        this.date = date;
    }

    public Boolean isPassed() {
        return isPassed;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}