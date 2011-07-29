package magic.model;

public class MagicLogMessageBuilder {

	private final MagicGame game;
	private final StringBuilder messageBuilders[];
	
	public MagicLogMessageBuilder(final MagicGame game) {
		this.game=game;
		messageBuilders=new StringBuilder[]{new StringBuilder(),new StringBuilder()};
	}
	
	public void appendMessage(final MagicPlayer player,final String message) {
		final StringBuilder messageBuilder=messageBuilders[player.getIndex()];
		if (messageBuilder.length()>0) {
			messageBuilder.append(' ');
		}
		messageBuilder.append(message);
	}
	
	public void logMessages() {
		for (final MagicPlayer player : game.getPlayers()) {
			final StringBuilder messageBuilder=messageBuilders[player.getIndex()];
			if (messageBuilder.length()>0) {
				game.getLogBook().add(new MagicMessage(game,player,messageBuilder.toString()));
				messageBuilder.setLength(0);
			}
		}
	}
	
	public void clearMessages() {
		messageBuilders[0].setLength(0);
		messageBuilders[1].setLength(0);
	}
}
