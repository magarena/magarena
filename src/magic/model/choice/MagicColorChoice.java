package magic.model.choice;

import magic.model.*;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.ui.GameController;
import magic.ui.choice.ColorChoicePanel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Contains optimal decision logic for each choice type. */
public class MagicColorChoice extends MagicChoice {

	private static final int ALL=0;
	private static final int MOST=1;
	private static final int UNSUMMON=2;
	private static final int BLUE_RED_WHITE=3;
	
	private static final List<Object> COLOR_OPTIONS=Arrays.<Object>asList(
			MagicColor.Black,MagicColor.Blue,MagicColor.Green,MagicColor.Red,MagicColor.White);
	
	private static final List<Object> BLUE_RED_WHITE_OPTIONS=Arrays.<Object>asList(
			MagicColor.Blue,MagicColor.Red,MagicColor.White);
	
	public static final MagicColorChoice ALL_INSTANCE=new MagicColorChoice(ALL);
	public static final MagicColorChoice MOST_INSTANCE=new MagicColorChoice(MOST);
	public static final MagicColorChoice UNSUMMON_INSTANCE=new MagicColorChoice(UNSUMMON);
	public static final MagicColorChoice BLUE_RED_WHITE_INSTANCE=new MagicColorChoice(BLUE_RED_WHITE);
		
	private final int type;
	
	private MagicColorChoice(final int type) {
		
		super("Choose yes or no.");
		this.type=type;
	}
	
	private Collection<Object> getArtificialMostOptions(final MagicGame game,final MagicPlayer player) {

		final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_PERMANENT);
		final int counts[]=new int[MagicColor.NR_COLORS];
		for (final MagicTarget target : targets) {
			
			final MagicPermanent permanent=(MagicPermanent)target;
			final int colorFlags=permanent.getColorFlags();
			for (final MagicColor color : MagicColor.values()) {
				
				if (color.hasColor(colorFlags)) {
					counts[color.ordinal()]++;
				}
			}
		}

		int bestCount=Integer.MIN_VALUE;
		MagicColor bestColor=null;
		for (final MagicColor color : MagicColor.values()) {
			
			final int count=counts[color.ordinal()];
			if (count>bestCount) {
				bestCount=count;
				bestColor=color;
			}
		}
		return Collections.<Object>singletonList(bestColor);
	}
	
	private Collection<Object> getArtificialUnsummonOptions(final MagicGame game,final MagicPlayer player) {

		final Collection<MagicTarget> targets=game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE);
		final int scores[]=new int[MagicColor.NR_COLORS];
		for (final MagicTarget target : targets) {
			
			final MagicPermanent permanent=(MagicPermanent)target;
			int score=permanent.getScore(game);
			if (permanent.getController()==player) {
				score=-score;
			}
			final int colorFlags=permanent.getColorFlags();
			for (final MagicColor color : MagicColor.values()) {
				
				if (color.hasColor(colorFlags)) {
					scores[color.ordinal()]+=score;
				}
			}			
		}
		
		int bestScore=Integer.MIN_VALUE;
		MagicColor bestColor=null;
		for (final MagicColor color : MagicColor.values()) {
			
			final int score=scores[color.ordinal()];
			if (score>bestScore) {
				bestScore=score;
				bestColor=color;
			}
		}
		return Collections.<Object>singletonList(bestColor);
	}
	
	@Override
	public Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {

		switch (type) {
			case MOST: return getArtificialMostOptions(game,player);
			case UNSUMMON: return getArtificialUnsummonOptions(game,player);
			case BLUE_RED_WHITE: return BLUE_RED_WHITE_OPTIONS;
			default: return COLOR_OPTIONS;
		}
	}	
	
	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {
		
        final ColorChoicePanel choicePanel=new ColorChoicePanel(controller,source);
		controller.disableActionButton(false);
		controller.showComponent(choicePanel);
		if (controller.waitForInputOrUndo()) {
			return UNDO_CHOICE_RESULTS;
		}
		return new Object[]{choicePanel.getColor()};
	}
}
