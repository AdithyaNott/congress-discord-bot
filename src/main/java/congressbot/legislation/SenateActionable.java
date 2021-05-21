package congressbot.legislation;

import java.util.Date;

public interface SenateActionable {

    public void recordSenateVote(boolean isPassed, Date date);

}