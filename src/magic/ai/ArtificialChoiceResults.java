package magic.ai;

public class ArtificialChoiceResults {

    final Object[] choiceResults;
    ArtificialScore aiScore=ArtificialScore.INVALID_SCORE;
    long worker=-1;
    int gameCount=1;

    ArtificialChoiceResults(final Object[] choiceResults) {
        this.choiceResults=choiceResults;
    }

    @Override
    public String toString() {
        final StringBuilder buffer=new StringBuilder();
        buffer.append('[').append(worker).append('/').append(gameCount).append('/').append(aiScore).append(']');
        appendResult(choiceResults, buffer);
        return buffer.toString();
    }

    public static void appendResult(final Object[] result, final StringBuilder buffer) {
        if (result!=null) {
            buffer.append(" (");
            boolean first=true;
            for (final Object obj : result) {
                if (first) {
                    first=false;
                } else {
                    buffer.append(',');
                }
                buffer.append(obj);
            }
            buffer.append(')');
        }
    }
}
