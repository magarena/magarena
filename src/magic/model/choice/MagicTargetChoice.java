package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetNone;
import magic.model.target.MagicTargetType;
import magic.ui.GameController;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MagicTargetChoice extends MagicChoice {

	public static final MagicTargetChoice TARGET_SPELL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_SPELL,true,MagicTargetHint.None,
                "target spell");
	public static final MagicTargetChoice NEG_TARGET_SPELL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_SPELL,true,MagicTargetHint.Negative,
                "target spell");
	public static final MagicTargetChoice NEG_TARGET_RED_GREEN_SPELL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_RED_GREEN_SPELL,true,MagicTargetHint.Negative,
                "target red or green spell");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_SPELL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_SPELL,true,MagicTargetHint.Negative,
                "target creature spell");
	public static final MagicTargetChoice NEG_TARGET_NONCREATURE_SPELL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NONCREATURE_SPELL,true,MagicTargetHint.Negative,
                "target noncreature spell");
	public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_SPELL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_INSTANT_OR_SORCERY_SPELL,true,MagicTargetHint.None,
                "target instant or sorcery spell");
	public static final MagicTargetChoice NEG_TARGET_INSTANT_OR_SORCERY_SPELL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_INSTANT_OR_SORCERY_SPELL,true,MagicTargetHint.Negative,
                "target instant or sorcery spell");
	public static final MagicTargetChoice TARGET_PLAYER=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PLAYER,true,MagicTargetHint.None,
                "target player");
	public static final MagicTargetChoice TARGET_OPPONENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_OPPONENT,true,MagicTargetHint.None,
                "target opponent");
	public static final MagicTargetChoice POS_TARGET_PLAYER=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PLAYER,true,MagicTargetHint.Positive,
                "target player");
	public static final MagicTargetChoice NEG_TARGET_PLAYER=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PLAYER,true,MagicTargetHint.Negative,
                "target player");
	public static final MagicTargetChoice NEG_TARGET_SPELL_OR_PERMANENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_SPELL_OR_PERMANENT,true,MagicTargetHint.Negative,
                "target spell or permanent");
	public static final MagicTargetChoice TARGET_PERMANENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PERMANENT,true,MagicTargetHint.None,
                "target permanent");
	public static final MagicTargetChoice TARGET_PERMANENT_YOU_CONTROL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PERMANENT_YOU_CONTROL,true,MagicTargetHint.None,
                "target permanent you control");	
	public static final MagicTargetChoice NEG_TARGET_PERMANENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PERMANENT,true,MagicTargetHint.Negative,"target permanent");
	public static final MagicTargetChoice NEG_TARGET_BLACK_RED_PERMANENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_BLACK_RED_PERMANENT,true,MagicTargetHint.Negative,"target black or red permanent");
    
    public static final MagicTargetChoice TARGET_NONBASIC_LAND=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NONBASIC_LAND,true,MagicTargetHint.None,"target non basic land");
	public static final MagicTargetChoice NEG_TARGET_NONBASIC_LAND=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NONBASIC_LAND,true,MagicTargetHint.Negative,"target non basic land");

    public static final MagicTargetChoice TARGET_NONLAND_PERMANENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NONLAND_PERMANENT,true,MagicTargetHint.None,"target nonland permanent");
	public static final MagicTargetChoice NEG_TARGET_NONLAND_PERMANENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NONLAND_PERMANENT,true,MagicTargetHint.Negative,"target nonland permanent");
	public static final MagicTargetChoice NEG_TARGET_ARTIFACT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ARTIFACT,true,MagicTargetHint.Negative,"target artifact");
	public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ARTIFACT_OR_ENCHANTMENT,true,MagicTargetHint.None,"target artifact or enchantment");
	public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_ENCHANTMENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ARTIFACT_OR_ENCHANTMENT,true,MagicTargetHint.Negative,"target artifact or enchantment");
	public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ARTIFACT_OR_CREATURE,true,MagicTargetHint.Negative,"target artifact or creature");
	public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS,true,MagicTargetHint.None,
			"target artifact or enchantment your opponent controls");
	public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND,true,MagicTargetHint.None,"target artifact, enchantment or land");
	public static final MagicTargetChoice NEG_TARGET_ENCHANTMENT =
			new MagicTargetChoice(MagicTargetFilter.TARGET_ENCHANTMENT,true,MagicTargetHint.Negative,"target enchantment");
	public static final MagicTargetChoice TARGET_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE,true,MagicTargetHint.None,"target creature");
	public static final MagicTargetChoice NEG_TARGET_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE,true,MagicTargetHint.Negative,"target creature");
	public static final MagicTargetChoice POS_TARGET_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE,true,MagicTargetHint.Positive,"target creature");
	public static final MagicTargetChoice NEG_TARGET_NONBLACK_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NONBLACK_CREATURE,true,MagicTargetHint.Negative,"target nonblack creature");
	public static final MagicTargetChoice NEG_TARGET_NONARTIFACT_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NONARTIFACT_CREATURE,true,MagicTargetHint.Negative,"target nonartifact creature");
	public static final MagicTargetChoice NEG_TARGET_TAPPED_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_TAPPED_CREATURE,true,MagicTargetHint.Negative,"target tapped creature");
	public static final MagicTargetChoice NEG_TARGET_UNTAPPED_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_UNTAPPED_CREATURE,true,MagicTargetHint.Negative,"target untapped creature");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_CONVERTED_3_OR_LESS=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_CONVERTED_3_OR_LESS,true,MagicTargetHint.Negative,
		"target creature with converted mana cost 3 or less");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_WITH_FLYING=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_WITH_FLYING,true,MagicTargetHint.Negative,"target creature with flying");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_WITHOUT_FLYING=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING,true,MagicTargetHint.Negative,"target creature without flying");	
	public static final MagicTargetChoice NEG_TARGET_ATTACKING_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ATTACKING_CREATURE,true,MagicTargetHint.Negative,"target attacking creature");
	public static final MagicTargetChoice NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE =
			new MagicTargetChoice(MagicTargetFilter.TARGET_ATTACKING_OR_BLOCKING_CREATURE,true,MagicTargetHint.Negative,"target attacking creature");
	public static final MagicTargetChoice NEG_TARGET_ATTACKING_CREATURE_WITH_FLYING=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ATTACKING_CREATURE_WITH_FLYING,true,MagicTargetHint.Negative,"target attacking creature with flying");
	public static final MagicTargetChoice NEG_TARGET_BLOCKED_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_BLOCKED_CREATURE,true,MagicTargetHint.Negative,"target blocked creature");
	public static final MagicTargetChoice CREATURE_YOU_CONTROL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,false,MagicTargetHint.None,"a creature you control");
	public static final MagicTargetChoice RED_OR_GREEN_CREATURE_YOU_CONTROL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_RED_OR_GREEN_CREATURE_YOU_CONTROL,false,MagicTargetHint.None,"a red or green creature you control");
	public static final MagicTargetChoice NEG_GREEN_OR_WHITE_CREATURE =
		new MagicTargetChoice(MagicTargetFilter.TARGET_GREEN_OR_WHITE_CREATURE,true,MagicTargetHint.Negative,"target green or white creature");
	public static final MagicTargetChoice TARGET_CREATURE_YOU_CONTROL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,true,MagicTargetHint.None,"target creature you control");
	public static final MagicTargetChoice TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL=
		new MagicTargetChoice(MagicTargetFilter.TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL,true,MagicTargetHint.None,"target nonlegendary creature you control");
	public static final MagicTargetChoice TARGET_CREATURE_YOUR_OPPONENT_CONTROLS=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,true,MagicTargetHint.None,"target creature your opponent controls");
	public static final MagicTargetChoice TARGET_CREATURE_OR_PLAYER=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_OR_PLAYER,true,MagicTargetHint.None,"target creature or player");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_PLAYER=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_OR_PLAYER,true,MagicTargetHint.Negative,"target creature or player");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_LAND=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_OR_LAND,true,MagicTargetHint.Negative,"target creature or land");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_ARTIFACT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_OR_ARTIFACT,true,MagicTargetHint.Negative,"target artifact or creature");
	public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_ENCHANTMENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_OR_ENCHANTMENT,true,MagicTargetHint.Negative,"target creature or enchantment");
	public static final MagicTargetChoice POS_TARGET_CREATURE_OR_PLAYER=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_OR_PLAYER,true,MagicTargetHint.Positive,"target creature or player");
	public static final MagicTargetChoice SACRIFICE_PERMANENT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PERMANENT_YOU_CONTROL,false,MagicTargetHint.None,"a permanent to sacrifice");
	public static final MagicTargetChoice SACRIFICE_CREATURE=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,false,MagicTargetHint.None,"a creature to sacrifice");
	public static final MagicTargetChoice SACRIFICE_BAT=
		new MagicTargetChoice(MagicTargetFilter.TARGET_BAT_YOU_CONTROL,false,MagicTargetHint.None,"a Bat to sacrifice");	
	public static final MagicTargetChoice SACRIFICE_BEAST=
		new MagicTargetChoice(MagicTargetFilter.TARGET_BEAST_YOU_CONTROL,false,MagicTargetHint.None,"a Beast to sacrifice");
	public static final MagicTargetChoice SACRIFICE_GOBLIN=
		new MagicTargetChoice(MagicTargetFilter.TARGET_GOBLIN_YOU_CONTROL,false,MagicTargetHint.None,"a Goblin to sacrifice");
	public static final MagicTargetChoice SACRIFICE_NON_ZOMBIE=
			new MagicTargetChoice(MagicTargetFilter.TARGET_NON_ZOMBIE_YOU_CONTROL,false,MagicTargetHint.None,"a non-Zombie creature to sacrifice");
	public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_GRAVEYARD=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD,false,MagicTargetHint.None,"target creature card from your graveyard");
	public static final MagicTargetChoice TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD=
		new MagicTargetChoice(MagicTargetFilter.TARGET_PERMANENT_CARD_CMC_LEQ_3_FROM_GRAVEYARD,false,MagicTargetHint.None,"target permanent card with converted mana cost 3 or less from your graveyard");
	public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD,false,MagicTargetHint.None,"target creature card from your opponent's graveyard");
	public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD=
		new MagicTargetChoice(MagicTargetFilter.TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD,false,MagicTargetHint.None,"target instant or sorcery card from your graveyard");
	public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD=
		new MagicTargetChoice(MagicTargetFilter.TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD,false,MagicTargetHint.None,"target instant or sorcery card from your opponent's graveyard");
	public static final MagicTargetChoice TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD =
			new MagicTargetChoice(MagicTargetFilter.TARGET_ENCHANTMENT_CARD_FROM_GRAVEYARD,false,MagicTargetHint.None,"target enchantment card from your graveyard");
	public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS=
		new MagicTargetChoice(MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,false,MagicTargetHint.None,"target creature card from a graveyard");
	public static final MagicTargetChoice TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS=
		new MagicTargetChoice(MagicTargetFilter.TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS,false,MagicTargetHint.None,"target artifact or creature card from a graveyard");
	public static final MagicTargetChoice TARGET_GOBLIN_CARD_FROM_GRAVEYARD=
		new MagicTargetChoice(MagicTargetFilter.TARGET_GOBLIN_CARD_FROM_GRAVEYARD,false,MagicTargetHint.None,"target Goblin card from your graveyard");
		
	private final String targetDescription;
	private final MagicTargetFilter targetFilter;
	private final boolean targeted;
	private final MagicTargetHint targetHint;
	
	public MagicTargetChoice(
            final MagicTargetFilter targetFilter,
            final boolean targeted,
            final MagicTargetHint hint,
            final String targetDesciption) {
		super("Choose "+targetDesciption+'.');
		this.targetDescription=targetDesciption;
		this.targetFilter=targetFilter;
		this.targeted=targeted;
		this.targetHint=hint;
	}
	
	public final String getTargetDescription() {
		return targetDescription;
	}

	public final MagicTargetFilter getTargetFilter() {
		return targetFilter;
	}
	
	public final boolean isTargeted() {
		return targeted;
	}

	public final MagicTargetHint getTargetHint(final boolean hints) {
		return hints?targetHint:MagicTargetHint.None;
	}
	
	@Override
	public final MagicTargetChoice getTargetChoice() {
		return this;
	}

	@Override
	public final boolean hasOptions(
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source,
            final boolean hints) {
		return game.hasLegalTargets(player,source,this,hints);
	}
	
	@Override
	public final Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		final Collection<Object> targets=game.getLegalTargets(player,source,this,targetHint);
		if (game.getFastChoices()) {
			return event.getTargetPicker().pickTargets(game,player,targets);
		}
		return targets;
	}

	@Override
	public final Object[] getPlayerChoiceResults(final GameController controller,final MagicGame game,
            final MagicPlayer player,final MagicSource source) {

		controller.disableActionButton(false);		
		controller.showMessage(source,getDescription());
		if (targetFilter.acceptType(MagicTargetType.Graveyard)) {
			controller.focusViewers(1,-1);
		} else if (targetFilter.acceptType(MagicTargetType.OpponentsGraveyard)) {
			controller.focusViewers(2,-1);
		}	
		final MagicTargetHint usedTargetHint=getTargetHint(GeneralConfig.getInstance().getSmartTarget());
		final Set<Object> validChoices=new HashSet<Object>(game.getLegalTargets(player,source,this,usedTargetHint));
		if (validChoices.size()==1) {
			// There are no valid choices.
			if (validChoices.contains(MagicTargetNone.getInstance())) {			
				return new Object[]{MagicTargetNone.getInstance()};
			} 
			// Only valid choice is player.
			if (validChoices.contains(player)) {
				return new Object[]{player};
			}
			// Only valid choice is opponent.
			final MagicPlayer opponent=game.getOpponent(player);
			if (validChoices.contains(opponent)) {
				return new Object[]{opponent};
			}
		}
		controller.setValidChoices(validChoices,false);
		if (controller.waitForInputOrUndo()) {
			return UNDO_CHOICE_RESULTS;
		}
		return new Object[]{controller.getChoiceClicked()};
	}
}
