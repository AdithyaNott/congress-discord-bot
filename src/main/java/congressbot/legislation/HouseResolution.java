package congressbot.legislation;

import java.util.Date;

public class HouseResolution extends CongressBill implements HouseActionable {
    private static final String EXPLANATION = "An H.Res. is a simple resolution originating from the House of Representatives. Simple Resolutions from the House of Representatives address matters entirely within the prerogative of the House of Representatives. For example, they can be used to change House rules or to express a sentiment from the House of Representatives. They do not require approval by neither the Senate nor the President, and do not have the force of law.";

    public HouseResolution(CongressBill.CongressBillBuilder cb) {
        super(cb, EXPLANATION, LegislativeStep.newHouseVote());
    }

    @Override
    public void recordHouseVote(boolean isPassed, Date date) {
        setLegislativeStepStatus(0, isPassed, date);
    }

}
