package magic.model;

import magic.model.phase.MagicPhaseType;

import java.util.Collection;
import java.util.Iterator;

public class MagicMessage {

	private final MagicPlayer player;
	private final int life;
	private final int turn;
	private final MagicPhaseType phaseType;
	private final String text;
	
	MagicMessage(final MagicGame game,final MagicPlayer player,final String text) {
		this.player=player;
		this.life=player.getLife();
		this.turn=game.getTurn();
		this.phaseType=game.getPhase().getType();
		this.text=text;
	}

	public MagicPlayer getPlayer() {
		return player;
	}
	
	public int getLife() {
		return life;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public MagicPhaseType getPhaseType() {
		return phaseType;
	}
	
	public String getText() {
		return text;
	}

	static void addNames(final StringBuilder builder,final Collection<String> names) {
		if (!names.isEmpty()) {
			boolean first=true;
			boolean next;
			final Iterator<String> iterator=names.iterator();
			do {
				final String name=iterator.next();
				next=iterator.hasNext();									
				if (first) {
					first=false;
				} else if (next) {
					builder.append(", ");
				} else {
					builder.append(" and ");
				}
				builder.append(name);
			} while (next);
		}
	}
	
	public static String replaceChoices(final String sourceText,final Object choices[]) {
		final String parts[]=sourceText.split("\\$");
		if (parts.length<2) {
			return sourceText;
		}

		final StringBuilder text=new StringBuilder();
		for (int index=0;index<parts.length;index++) {
			
			text.append(parts[index]);
			if (choices!=null&&index<choices.length) {
				final Object choice=choices[index];
				if (choice!=null) {
					final String choiceText=choice.toString();
					if (!choiceText.isEmpty()) {
						text.append(" (").append(choiceText).append(")");
					}
				}
			}
		}
		return text.toString();
	}	
}
