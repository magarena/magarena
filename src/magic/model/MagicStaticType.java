package magic.model;

public enum MagicStaticType {

	None("none"),
	All("all"),
	Player("player"),
	Opponent("opponent")
	;
	
	private final String name;
	
	private MagicStaticType(final String name) {
		this.name=name;
	}
	
	private String getName() {
		return name;
	}
	
	public int getScore(final MagicGame game,final MagicPermanent scorePermanent) {
		if (this==None) {
			return 0;
		}

		int score=0;
		final MagicPlayer player=scorePermanent.getController();
		if (this==All||this==Player) {
			for (final MagicPermanent permanent : player.getPermanents()) {
				
				if (permanent.isCreature()&&permanent!=scorePermanent) {
					score+=permanent.getScore(game);
				}
			}
		}
		if (this==All||this==Opponent) {
			for (final MagicPermanent permanent : game.getOpponent(player).getPermanents()) {
				
				if (permanent.isCreature()) {
					score-=permanent.getScore(game);
				}
			}
		}
		return score;
	}
	
	public static MagicStaticType getStaticTypeFor(final String name) {
		for (final MagicStaticType type : values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return MagicStaticType.None;
	}
}
