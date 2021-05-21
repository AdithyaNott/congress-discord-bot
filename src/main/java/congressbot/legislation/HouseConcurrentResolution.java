package congressbot.legislation;

import java.util.Date;

public class HouseConcurrentResolution extends CongressBill implements HouseActionable, SenateActionable {
    private static final String EXPLANATION = "An H.Con.Res. is a concurrent resolution originating from the House of Representatives. Concurrent Resolutions are generally used to make or change rules that apply to both chambers of Congress or to jointly express a sentiment from Congress. While they must pass both chambers of Congress, they do not require the president's signature and do not have the force of law.";

    public HouseConcurrentResolution(CongressBill.CongressBillBuilder cb) {
        super(cb, EXPLANATION,
                LegislativeStep.newHouseVote(),
                LegislativeStep.newSenateVote());
    }

    @Override
    public void recordHouseVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(0, isPassed, date);
    }

    @Override
    public void recordSenateVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(1, isPassed, date);
    }

}
