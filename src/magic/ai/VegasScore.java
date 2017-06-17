package magic.ai;

public class VegasScore {

    private final Object[] choiceResults;
    private long totalScore;
    private int count;

    VegasScore(final Object[] choiceResults) {
        this.choiceResults=choiceResults;
    }

    void incrementScore(final int score) {
        totalScore+=score;
        count++;
    }

    Object[] getChoiceResults() {
        return choiceResults;
    }

    @Override
    public String toString() {
        final StringBuilder buffer=new StringBuilder();
        buffer.append("[").append(getScore()).append('/').append(count).append("]");
        if (choiceResults!=null) {
            buffer.append(" (");
            boolean first=true;
            for (final Object choiceResult : choiceResults) {
                if (first) {
                    first=false;
                } else {
                    buffer.append(',');
                }
                buffer.append(choiceResult);
            }
            buffer.append(')');
        }
        return buffer.toString();
    }

    int getScore() {
        return count>0?(int)(totalScore/count):ArtificialScoringSystem.LOSE_GAME_SCORE;
    }
}
