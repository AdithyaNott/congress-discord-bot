package congressbot.legislation;

import java.util.Date;

public class SenateJointResolution extends CongressBill implements Vetoable {
    private static final String EXPLANATION = "An S.J.Res. is a joint resolution originating from the Senate. They are generally the same as bills in that they require approval of both chambers of Congress and the president's signature (or if vetoed by the president, 2/3 of both chambers of Congress overriding the veto) to become a law. A notable exception is Joint Resolutions that are proposing Constitutional Amendments, which require 2/3 approval from both chambers of Congress and 3/4 of the states, but do not require the president's signature.";

    public SenateJointResolution(CongressBill.CongressBillBuilder cb) {
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