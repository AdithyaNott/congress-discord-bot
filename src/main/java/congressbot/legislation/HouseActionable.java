package congressbot.legislation;

import java.util.Date;

public interface HouseActionable {

    public void recordHouseVote(boolean isPassed, Date date);

}