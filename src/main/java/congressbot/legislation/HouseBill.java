package congressbot.legislation;

import java.util.Date;

public class HouseBill extends CongressBill implements Vetoable {
    private static final String EXPLANATION = "An H.R. is a bill originating from the House of Representatives. Bills can deal with appropriations, domestic issues, government programs, and more. A bill requires approval from both chambers of Congress and the signature of the president (or if vetoed by the president, 2/3 of both chambers of Congress overriding the veto) to pass and become a law.";

    public HouseBill(CongressBillBuilder cb) {
        super(cb, EXPLANATION,
                LegislativeStep.newHouseVote(),
                LegislativeStep.newSenateVote(),
                LegislativeStep.newPresidentAction(),
                null,
                null);
    }

    @Override
    public void recordHouseVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(0, isPassed, date);
    }

    @Override
    public void recordSenateVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(1, isPassed, date);
    }

    @Override
    public void recordPresidentAction(boolean isPassed, Date date) {
        setLegislativeStepStatus(2, isPassed, date);
        if (!isPassed && (null == getLegislativeStep(3)) && (null == getLegislativeStep(4))) {
            setLegislativeStep(3, LegislativeStep.newHouseOverride());
            setLegislativeStep(4, LegislativeStep.newSenateOverride());
        }
    }

    @Override
    public void recordHouseOverride(boolean isPassed, Date date) {
        setLegislativeStepStatus(3, isPassed, date);
    }

    @Override
    public void recordSenateOverride(boolean isPassed, Date date) {
        setLegislativeStepStatus(4, isPassed, date);
    }
}