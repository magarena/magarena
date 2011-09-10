package magic.ai;

public class VegasScore {

	private final Object choiceResults[];
	private long totalScore=0;
	private int count=0;
	
	VegasScore(final Object choiceResults[]) {
		this.choiceResults=choiceResults;
	}
	
	synchronized void incrementScore(final int score) {
		totalScore+=score;
		count++;
	}

	Object[] getChoiceResults() {
		return choiceResults;
	}
	
	synchronized int getScore() {
		return count>0?(int)(totalScore/count):ArtificialScoringSystem.LOSE_GAME_SCORE;
	}
}
