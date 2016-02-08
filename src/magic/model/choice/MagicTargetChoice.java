package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTargetNone;
import magic.model.target.MagicTargetPicker;
import magic.model.target.MagicTargetType;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.exception.UndoClickedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import magic.model.IUIGameController;

public class MagicTargetChoice extends MagicChoice {
    public static final MagicTargetChoice NONE =
        new MagicTargetChoice(MagicTargetFilterFactory.NONE,MagicTargetHint.None,"nothing") {
            @Override
            public boolean isValid() {
                return false;
            }
        };

    public static final MagicTargetChoice A_PAYABLE_CREATURE_CARD_FROM_YOUR_GRAVEYARD =
        new MagicTargetChoice("a creature card with scavenge from your graveyard");

    public static final MagicTargetChoice TARGET_SPELL =
        new MagicTargetChoice("target spell");

    public static final MagicTargetChoice NEG_TARGET_SPELL =
        MagicTargetChoice.Negative("target spell");

    public static final MagicTargetChoice NEG_TARGET_CREATURE_SPELL =
        MagicTargetChoice.Negative("target creature spell");

    public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_SPELL =
        new MagicTargetChoice("target instant or sorcery spell");

    public static final MagicTargetChoice NEG_TARGET_INSTANT_SPELL =
        MagicTargetChoice.Negative("target instant spell");

    public static final MagicTargetChoice TARGET_PLAYER =
        new MagicTargetChoice("target player");

    public static final MagicTargetChoice TARGET_PLAYER_CONTROLS_CREATURE =
        new MagicTargetChoice("target player that controls a creature");

    public static final MagicTargetChoice TARGET_OPPONENT =
        new MagicTargetChoice("target opponent");

    public static final MagicTargetChoice POS_TARGET_PLAYER =
        MagicTargetChoice.Positive("target player");

    public static final MagicTargetChoice NEG_TARGET_PLAYER =
        MagicTargetChoice.Negative("target player");

    public static final MagicTargetChoice NEG_TARGET_SPELL_OR_PERMANENT =
        MagicTargetChoice.Negative("target spell or permanent");

    public static final MagicTargetChoice TARGET_PERMANENT =
        new MagicTargetChoice("target permanent");

    public static final MagicTargetChoice A_PERMANENT_YOU_CONTROL =
        new MagicTargetChoice("a permanent you control");

    public static final MagicTargetChoice TARGET_PERMANENT_YOU_CONTROL =
        new MagicTargetChoice("target permanent you control");

    public static final MagicTargetChoice TARGET_PERMANENT_YOU_OWN =
        new MagicTargetChoice("target permanent you own");

    public static final MagicTargetChoice NEG_TARGET_PERMANENT =
        MagicTargetChoice.Negative("target permanent");

    public static final MagicTargetChoice TARGET_LAND =
        new MagicTargetChoice("target land");

    public static final MagicTargetChoice NEG_TARGET_LAND =
        MagicTargetChoice.Negative("target land");

    public static final MagicTargetChoice A_LAND_YOU_CONTROL =
        new MagicTargetChoice("a land you control");

    public static final MagicTargetChoice FOREST_YOU_CONTROL =
            new MagicTargetChoice("a Forest you control");

    public static final MagicTargetChoice ISLAND_YOU_CONTROL =
        new MagicTargetChoice("an Island you control");

    public static final MagicTargetChoice TARGET_NONLAND_PERMANENT =
        new MagicTargetChoice("target nonland permanent");

    public static final MagicTargetChoice TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS =
        new MagicTargetChoice("target nonland permanent an opponent controls");

    public static final MagicTargetChoice NEG_TARGET_NONLAND_PERMANENT =
        MagicTargetChoice.Negative("target nonland permanent");

    public static final MagicTargetChoice TARGET_ARTIFACT =
        new MagicTargetChoice("target artifact");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT =
        MagicTargetChoice.Negative("target artifact");

    public static final MagicTargetChoice POS_TARGET_ARTIFACT =
        MagicTargetChoice.Positive("target artifact");

    public static final MagicTargetChoice TARGET_NONCREATURE_ARTIFACT =
        new MagicTargetChoice("target noncreature artifact");

    public static final MagicTargetChoice POS_TARGET_NONCREATURE_ARTIFACT =
        MagicTargetChoice.Positive("target noncreature artifact");

    public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT =
        new MagicTargetChoice("target artifact or enchantment");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_ENCHANTMENT =
        MagicTargetChoice.Negative("target artifact or enchantment");

    public static final MagicTargetChoice AN_ARTIFACT_OR_CREATURE =
        new MagicTargetChoice("an artifact or creature");

    public static final MagicTargetChoice TARGET_ARTIFACT_OR_CREATURE =
        new MagicTargetChoice("target artifact or creature");

    public static final MagicTargetChoice NEG_TARGET_ARTIFACT_OR_CREATURE =
        MagicTargetChoice.Negative("target artifact or creature");

    public static final MagicTargetChoice TARGET_ARTIFACT_OR_ENCHANTMENT_YOUR_OPPONENT_CONTROLS =
        new MagicTargetChoice("target artifact or enchantment an opponent controls");

    public static final MagicTargetChoice POS_TARGET_ARTIFACT_CREATURE =
        MagicTargetChoice.Positive("target artifact creature");

    public static final MagicTargetChoice TARGET_ENCHANTMENT =
        new MagicTargetChoice("target enchantment");

    public static final MagicTargetChoice TARGET_ENCHANTMENT_YOU_CONTROL =
        new MagicTargetChoice("target enchantment you control");

    public static final MagicTargetChoice TARGET_ENCHANTMENT_YOUR_OPPONENT_CONTROLS =
        new MagicTargetChoice("target enchantment an opponent controls");

    public static final MagicTargetChoice NEG_TARGET_ENCHANTMENT =
        MagicTargetChoice.Negative("target enchantment");

    public static final MagicTargetChoice TARGET_CREATURE =
        new MagicTargetChoice("target creature");

    public static final MagicTargetChoice NEG_TARGET_CREATURE =
        MagicTargetChoice.Negative("target creature");

    public static final MagicTargetChoice POS_TARGET_CREATURE =
        MagicTargetChoice.Positive("target creature");

    public static final MagicTargetChoice TARGET_NONCREATURE =
        new MagicTargetChoice("target noncreature");

    public static final MagicTargetChoice NEG_TARGET_NONCREATURE =
        MagicTargetChoice.Negative("target noncreature");

    public static final MagicTargetChoice NEG_TARGET_NONBLACK_CREATURE =
        MagicTargetChoice.Negative("target nonblack creature");

    public static final MagicTargetChoice NEG_TARGET_UNTAPPED_CREATURE =
        MagicTargetChoice.Negative("target untapped creature");

    public static final MagicTargetChoice A_CREATURE_YOU_CONTROL =
        new MagicTargetChoice("a creature you control");

    public static final MagicTargetChoice A_CREATURE =
        new MagicTargetChoice("a creature");

    public static final MagicTargetChoice POS_CREATURE =
        MagicTargetChoice.Positive("a creature");

    public static final MagicTargetChoice RED_OR_GREEN_CREATURE_YOU_CONTROL =
        new MagicTargetChoice("a red or green creature you control");

    public static final MagicTargetChoice TARGET_CREATURE_PLUSONE_COUNTER =
        new MagicTargetChoice("target creature with a +1/+1 counter on it");

    public static final MagicTargetChoice TARGET_CREATURE_WITH_FLYING =
        new MagicTargetChoice("target creature with flying");

    public static final MagicTargetChoice NEG_TARGET_CREATURE_WITH_FLYING =
        MagicTargetChoice.Negative("target creature with flying");

    public static final MagicTargetChoice NEG_TARGET_ATTACKING_CREATURE =
        MagicTargetChoice.Negative("target attacking creature");

    public static final MagicTargetChoice POS_TARGET_ATTACKING_CREATURE =
        MagicTargetChoice.Positive("target attacking creature");

    public static final MagicTargetChoice TARGET_ATTACKING_OR_BLOCKING_CREATURE =
        new MagicTargetChoice("target attacking or blocking creature");

    public static final MagicTargetChoice NEG_WHITE_OR_BLUE_CREATURE =
        MagicTargetChoice.Negative("target white or blue creature");

    public static final MagicTargetChoice TARGET_CREATURE_YOU_CONTROL =
        new MagicTargetChoice("target creature you control");

    public static final MagicTargetChoice NEG_TARGET_EQUIPPED_CREATURE =
        MagicTargetChoice.Negative("target creature that is equipped");

    public static final MagicTargetChoice TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL =
        new MagicTargetChoice("target nonlegendary creature you control");

    public static final MagicTargetChoice TARGET_CREATURE_OR_PLAYER =
        new MagicTargetChoice("target creature or player");

    public static final MagicTargetChoice NEG_TARGET_CREATURE_OR_PLAYER =
        MagicTargetChoice.Negative("target creature or player");

    public static final MagicTargetChoice POS_TARGET_CREATURE_OR_PLAYER =
        MagicTargetChoice.Positive("target creature or player");

    public static final MagicTargetChoice NEG_TARGET_VAMPIRE =
        MagicTargetChoice.Negative("target Vampire");

    public static final MagicTargetChoice SACRIFICE_PERMANENT =
        new MagicTargetChoice("a permanent to sacrifice");

    public static final MagicTargetChoice SACRIFICE_CREATURE =
        new MagicTargetChoice("a creature to sacrifice");

    public static final MagicTargetChoice SACRIFICE_ARTIFACT =
        new MagicTargetChoice("an artifact to sacrifice");

    public static final MagicTargetChoice SACRIFICE_LAND =
        new MagicTargetChoice("a land to sacrifice");

    public static final MagicTargetChoice SACRIFICE_ENCHANTMENT =
        new MagicTargetChoice("an enchantment to sacrifice");

    public static final MagicTargetChoice SACRIFICE_FOREST =
        new MagicTargetChoice("a Forest to sacrifice");

    public static final MagicTargetChoice SACRIFICE_GOBLIN =
        new MagicTargetChoice("a Goblin to sacrifice");

    public static final MagicTargetChoice SACRIFICE_MERFOLK =
        new MagicTargetChoice("a Merfolk to sacrifice");

    public static final MagicTargetChoice SACRIFICE_CLERIC =
        new MagicTargetChoice("a Cleric creature to sacrifice");

    public static final MagicTargetChoice TARGET_CARD_FROM_GRAVEYARD =
        new MagicTargetChoice("target card from your graveyard");

    public static final MagicTargetChoice NEG_TARGET_CARD_FROM_ALL_GRAVEYARDS =
        MagicTargetChoice.Negative("target card from a graveyard");

    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_GRAVEYARD =
        new MagicTargetChoice("target creature card from your graveyard");

    public static final MagicTargetChoice AN_UNBLOCKED_ATTACKING_CREATURE_YOU_CONTROL =
        new MagicTargetChoice("an unblocked attacking creature you control");

    public static final MagicTargetChoice TARGET_CARD_FROM_OPPONENTS_GRAVEYARD =
        new MagicTargetChoice("target card from an opponent's graveyard");

    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_OPPONENTS_GRAVEYARD =
        new MagicTargetChoice("target creature card from an opponent's graveyard");

    public static final MagicTargetChoice TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD =
        new MagicTargetChoice("target instant or sorcery card from an opponent's graveyard");

    public static final MagicTargetChoice TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS =
        new MagicTargetChoice("target creature card from a graveyard");

    public static final MagicTargetChoice A_CARD_FROM_HAND =
        new MagicTargetChoice("a card from your hand");

    public static final MagicTargetChoice A_CARD_FROM_LIBRARY =
        new MagicTargetChoice("a card from your library");

    public static final MagicTargetChoice A_CARD_FROM_GRAVEYARD =
        new MagicTargetChoice("a card from your graveyard");

    public static final MagicTargetChoice A_CREATURE_CARD_FROM_HAND =
        new MagicTargetChoice("a creature card from your hand");

    public static final MagicTargetChoice A_GREEN_CREATURE_CARD_FROM_HAND =
        new MagicTargetChoice("a green creature card from your hand");

    public static final MagicTargetChoice A_BASIC_LAND_CARD_FROM_LIBRARY =
        new MagicTargetChoice("a basic land card from your library");

    public static final MagicTargetChoice A_CREATURE_CARD_FROM_LIBRARY =
        new MagicTargetChoice("a creature card from your library");

    public static final MagicTargetChoice SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY =
        new MagicTargetChoice("a Swamp or Mountain card from your library");

    public static final MagicTargetChoice PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY =
        new MagicTargetChoice("a Plains, Island, Swamp, or Mountain card from your library");

    public static final MagicTargetChoice LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY =
        new MagicTargetChoice("a land card with a basic land type from your library");

    public static final MagicTargetChoice LAND_CARD_FROM_HAND =
        new MagicTargetChoice("a land card from your hand");

    public static final MagicTargetChoice CREATURE_TOKEN_YOU_CONTROL =
        new MagicTargetChoice("a creature token you control");

    public static final MagicTargetChoice PLANESWALKER_YOUR_OPPONENT_CONTROLS =
        new MagicTargetChoice("a planeswalker an opponent controls");

    public static final MagicTargetChoice TARGET_CREATURE_YOUR_OPPONENT_CONTROLS =
        new MagicTargetChoice("target creature an opponent controls");

    public static final MagicTargetChoice TARGET_ARTIFACT_YOUR_OPPONENT_CONTROLS =
        new MagicTargetChoice("target artifact an opponent controls");

    public static final MagicTargetChoice ANOTHER_CREATURE_YOU_CONTROL =
        new MagicTargetChoice("another creature you control");

    private final String targetDescription;
    private final MagicTargetFilter<? extends MagicTarget> targetFilter;
    private final boolean targeted;
    private final MagicTargetHint targetHint;

    public static MagicTargetChoice Positive(final String aTargetDescription) {
        return new MagicTargetChoice(MagicTargetHint.Positive, aTargetDescription);
    }

    public static MagicTargetChoice Negative(final String aTargetDescription) {
        return new MagicTargetChoice(MagicTargetHint.Negative, aTargetDescription);
    }

    public static MagicTargetChoice Other(final String aTargetDescription, final MagicPermanent permanent) {
        return Other(aTargetDescription, permanent, MagicTargetHint.None);
    }

    public static MagicTargetChoice PosOther(final String aTargetDescription, final MagicPermanent permanent) {
        return Other(aTargetDescription, permanent, MagicTargetHint.Positive);
    }

    public static MagicTargetChoice NegOther(final String aTargetDescription, final MagicPermanent permanent) {
        return Other(aTargetDescription, permanent, MagicTargetHint.Negative);
    }

    private static MagicTargetChoice Other(final String aTargetDescription, final MagicPermanent permanent, final MagicTargetHint hint) {
        final MagicTargetChoice withoutOther = new MagicTargetChoice(aTargetDescription);
        final String compactDescription = aTargetDescription.replaceFirst("^a(n)? ", "");
        return new MagicTargetChoice(
            new MagicOtherPermanentTargetFilter(
                withoutOther.getPermanentFilter(),
                permanent
            ),
            hint,
            "another " + compactDescription
        );
    }

    public MagicTargetChoice(final String aTargetDescription) {
        this(MagicTargetHint.None, aTargetDescription);
    }

    private static String decapitalize(final String text) {
        return Character.toLowerCase(text.charAt(0)) + text.substring(1);
    }

    public MagicTargetChoice(final MagicTargetHint aTargetHint, final String aTargetDescription) {
        super("Choose " + decapitalize(aTargetDescription) + '.');
        targetHint        = aTargetHint;
        targetDescription = decapitalize(aTargetDescription);

        if (targetDescription.matches("target .*")) {
            targetFilter = MagicTargetFilterFactory.single(targetDescription.replaceFirst("target ", ""));
            targeted     = true;
        } else if (targetDescription.matches("another target .*")) {
            targetFilter = new MagicOtherPermanentTargetFilter(MagicTargetFilterFactory.Permanent(targetDescription.replaceFirst("another target ", "")));
            targeted     = true;
        } else if (targetDescription.matches("another .*")) {
            targetFilter = new MagicOtherPermanentTargetFilter(MagicTargetFilterFactory.Permanent(targetDescription.replaceFirst("another ", "")));
            targeted     = false;
        } else if (targetDescription.matches("a(n)? .*")) {
            targetFilter = MagicTargetFilterFactory.single(targetDescription.replaceFirst("a(n)? ", ""));
            targeted     = false;
        } else {
            throw new RuntimeException("unknown target choice: \"" + aTargetDescription + "\"");
        }
    }

    public MagicTargetChoice(
        final MagicTargetFilter<? extends MagicTarget> aTargetFilter,
        final String aTargetDescription
    ) {
        super("Choose " + aTargetDescription + '.');
        targetFilter = aTargetFilter;
        targeted     = aTargetDescription.contains("target");
        targetHint   = MagicTargetHint.None;
        targetDescription = aTargetDescription;
    }

    public MagicTargetChoice(
        final MagicTargetFilter<? extends MagicTarget> aTargetFilter,
        final MagicTargetHint aTargetHint,
        final String aTargetDescription
    ) {
        super("Choose " + aTargetDescription + '.');
        targetFilter = aTargetFilter;
        targeted     = aTargetDescription.contains("target");
        targetHint   = aTargetHint;
        targetDescription = aTargetDescription;
    }

    public MagicTargetChoice(
        final MagicTargetChoice copy,
        final boolean targeted
    ) {
        super("Choose "+copy.targetDescription+'.');
        this.targetFilter=copy.targetFilter;
        this.targeted=targeted;
        this.targetHint=copy.targetHint;
        this.targetDescription=copy.targetDescription;
    }

    public final String getTargetDescription() {
        return targetDescription;
    }

    @SuppressWarnings("unchecked")
    public final MagicTargetFilter<MagicTarget> getTargetFilter() {
        return (MagicTargetFilter<MagicTarget>)targetFilter;
    }

    @SuppressWarnings("unchecked")
    public final MagicTargetFilter<MagicCard> getCardFilter() {
        return (MagicTargetFilter<MagicCard>)targetFilter;
    }

    @SuppressWarnings("unchecked")
    public final MagicTargetFilter<MagicPermanent> getPermanentFilter() {
        return (MagicTargetFilter<MagicPermanent>)targetFilter;
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
    public final int getTargetChoiceResultIndex() {
        return 0;
    }

    @Override
    public long getStateId() {
        return magic.model.MurmurHash3.hash(new long[] {
            targetDescription.hashCode(),
            targetFilter.hashCode(),
            (targeted ? 1L : -1L),
            targetHint.hashCode()
        });
    }

    @Override
    public final boolean hasOptions(final MagicGame game, final MagicPlayer player, final MagicSource source, final boolean hints) {
        return game.hasLegalTargets(player,source,this,hints);
    }

    @Override
    final Collection<?> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
        Collection<MagicTarget> targets = game.getLegalTargets(player,source,this,targetHint);
        if (game.getFastTarget()) {
            @SuppressWarnings("unchecked")
            final MagicTargetPicker<MagicTarget> targetPicker = (MagicTargetPicker<MagicTarget>)event.getTargetPicker();
            targets = targetPicker.pickTargets(game,event,targets);
        }
        return targets;
    }

    @Override
    public final Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        controller.disableActionButton(false);
        controller.showMessage(source,getDescription());
        if (targetFilter.acceptType(MagicTargetType.Hand)) {
            controller.focusViewers(0);
        } else if (targetFilter.acceptType(MagicTargetType.Graveyard)) {
            controller.focusViewers(1);
        } else if (targetFilter.acceptType(MagicTargetType.OpponentsGraveyard)) {
            controller.focusViewers(2);
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
            final MagicPlayer opponent=player.getOpponent();
            if (validChoices.contains(opponent)) {
                return new Object[]{opponent};
            }
        }
        if (targetFilter.acceptType(MagicTargetType.Library)) {
            final MagicCardList cards = new MagicCardList();
            for (final Object card : validChoices) {
                cards.add((MagicCard)card);
            }
            controller.showCards(cards);
        }
        controller.setValidChoices(validChoices,false);
        controller.waitForInput();
        if (targetFilter.acceptType(MagicTargetType.Library)) {
            controller.clearCards();
        }
        return new Object[]{controller.getChoiceClicked()};
    }

}
