package congressbot.legislation;

import java.util.Date;

public class SenateBill extends CongressBill implements Vetoable {
    private static final String EXPLANATION = "An S. is a bill originating from the Senate. Bills can deal with appropriations, domestic issues, government programs, and more. A bill requires approval from both chambers of Congress and the signature of the president (or if vetoed by the president, 2/3 of both chambers of Congress overriding the veto) to pass and become a law.";

    public SenateBill(CongressBill.CongressBillBuilder cb) {
        super(cb, EXPLANATION,
                LegislativeStep.newSenateVote(),
                LegislativeStep.newHouseVote(),
                LegislativeStep.newPresidentAction(),
                null,
                null);
    }

    @Override
    public void recordSenateVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(0, isPassed, date);
    }

    @Override
    public void recordHouseVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(1, isPassed, date);
    }


    @Override
    public void recordPresidentAction(boolean isPassed, Date date) {
        setLegislativeStepStatus(2, isPassed, date);
        if (!isPassed && (null == getLegislativeStep(3)) && (null == getLegislativeStep(4))) {
            setLegislativeStep(3, LegislativeStep.newSenateOverride());
            setLegislativeStep(4, LegislativeStep.newHouseOverride());

        }
    }

    @Override
    public void recordSenateOverride(boolean isPassed, Date date) {
        setLegislativeStepStatus(3, isPassed, date);
    }

    @Override
    public void recordHouseOverride(boolean isPassed, Date date) {
        setLegislativeStepStatus(4, isPassed, date);
    }

}