package magic.ai;

public class ArtificialScore {

	public static final ArtificialScore INVALID_SCORE=new ArtificialScore(0,0);
	public static final ArtificialScore MAXIMUM_DEPTH_EXCEEDED_SCORE=new ArtificialScore(0,0);
	
	public int score;
	public int depth;
	
	public ArtificialScore(final int score,final int depth) {
		this.score=score;
		this.depth=depth;
	}

	public ArtificialScore getScore(final int depthIncr) {
		if (this==INVALID_SCORE) {
			return INVALID_SCORE;
		}
		return new ArtificialScore(score,depth+depthIncr);
	}
	
	public boolean isBetter(final ArtificialScore other,final boolean best) {
		if (other==INVALID_SCORE) {
			return false;
		} else if (this==INVALID_SCORE) {
			return true;
		} else if (score==other.score) {
			return depth>other.depth;
		} else if (best) {
			return score<other.score;
		} else {
			return score>other.score;
		}
	}
	
	@Override
	public String toString() {
		if (this==INVALID_SCORE) {
			return "none"; 
		}
		final StringBuffer buffer=new StringBuffer();
		buffer.append(score).append(" at ").append(depth);
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		return 31*score+depth;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null||getClass()!=obj.getClass()) {
			return false;
		}
		final ArtificialScore other=(ArtificialScore)obj;
		return score==other.score&&depth==other.depth;
	}
}
