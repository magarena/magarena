package magic.ai;

public class VegasScore {

    private final Object[] choiceResults;
    private long totalScore;
    private int count;
    
    VegasScore(final Object[] choiceResults) {
        this.choiceResults=choiceResults;
    }
    
    synchronized void incrementScore(final int score) {
        totalScore+=score;
        count++;
    }

    Object[] getChoiceResults() {
        return choiceResults;
    }
    
    public String toString() {
        final StringBuilder buffer=new StringBuilder();
        buffer.append("[").append(totalScore).append('/').append(count).append("]");
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
    
    synchronized int getScore() {
        return count>0?(int)(totalScore/count):ArtificialScoringSystem.LOSE_GAME_SCORE;
    }
}
