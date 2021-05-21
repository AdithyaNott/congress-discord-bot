package congressbot.legislation;

import java.util.Date;

public interface Vetoable extends HouseActionable, SenateActionable {

    public void recordPresidentAction(boolean isPassed, Date date);

    public void recordHouseOverride(boolean isPassed, Date date);

    public void recordSenateOverride(boolean isPassed, Date date);
}