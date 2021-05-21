package congressbot.legislation;

import java.util.Date;

public class SenateConcurrentResolution extends CongressBill implements HouseActionable, SenateActionable {
    private static final String EXPLANATION = "An S.Con.Res. is a concurrent resolution originating from the Senate. Concurrent Resolutions are generally used to make or change rules that apply to both chambers of Congress or to jointly express a sentiment from Congress. While they must pass both chambers of Congress, they do not require the president's signature and do not have the force of law.";
    private static final LegislativeStep[] STEPS = {LegislativeStep.newSenateVote(), LegislativeStep.newHouseVote()};

    public SenateConcurrentResolution(CongressBill.CongressBillBuilder cb) {
        super(cb, EXPLANATION, STEPS);
    }

    @Override
    public void recordSenateVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(0, isPassed, date);
    }

    @Override
    public void recordHouseVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(1, isPassed, date);
    }

}
