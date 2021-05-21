package congressbot.legislation;

import java.util.Date;

public class SenateResolution extends CongressBill implements SenateActionable {
    private static final String EXPLANATION = "An S.Res. is a simple resolution originating from the Senate. Simple Resolutions from the Senate address matters entirely within the prerogative of the Senate. For example, they can be used to change Senate rules or to express a sentiment from the Senate. They do not require approval by neither the House of Representatives nor the president, and do not have the force of law.";

    public SenateResolution(CongressBill.CongressBillBuilder cb) {
        super(cb, EXPLANATION, LegislativeStep.newSenateVote());
    }

    @Override
    public void recordSenateVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(0, isPassed, date);
    }

}
