package magic.ai;

public interface ArtificialPruneScore {
	
	public int getScore();
	
	public boolean pruneScore(final int score,final boolean best);
	
	public ArtificialPruneScore getPruneScore(final int score,final boolean best);	
}