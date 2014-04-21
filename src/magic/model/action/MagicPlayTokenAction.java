package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicStatic;
import magic.model.MagicAbility;
import magic.model.MagicPermanentState;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;
import magic.model.trigger.MagicAtEndOfCombatTrigger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MagicPlayTokenAction extends MagicPutIntoPlayAction {

    private final MagicCard card;
    private final List<MagicPlayMod> modifications;
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition, final List<MagicPlayMod> aModifications) {
        card=MagicCard.createTokenCard(cardDefinition,player);
        modifications=aModifications;
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition) {
        this(player, cardDefinition, Collections.<MagicPlayMod>emptyList());
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicCardDefinition cardDefinition,final MagicPlayMod... aModifications) {
        this(player, cardDefinition, Arrays.asList(aModifications));
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicObject obj) {
        this(player, obj.getCardDefinition(), Collections.<MagicPlayMod>emptyList());
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicObject obj, final List<MagicPlayMod> aModifications) {
        this(player, obj.getCardDefinition(), aModifications);
    }
    
    public MagicPlayTokenAction(final MagicPlayer player,final MagicObject obj, final MagicPlayMod... aModifications) {
        this(player, obj.getCardDefinition(), Arrays.asList(aModifications));
    }

    public MagicPlayTokenAction(final MagicCard aCard) {
        card=aCard;
        modifications=Collections.<MagicPlayMod>emptyList();
    }
    
    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        final MagicPermanent permanent=game.createPermanent(card,card.getController());
        for (final MagicPlayMod modification : modifications) {
            switch (modification) {
                case UNDYING:
                    permanent.changeCounters(MagicCounterType.PlusOne,1);
                    break;
                case PERSIST:
                    permanent.changeCounters(MagicCounterType.MinusOne,1);
                    break;
                case EXILE_AT_END_OF_COMBAT:
                    game.doAction(new MagicAddTriggerAction(permanent, MagicAtEndOfCombatTrigger.Exile));
                    break;
                case EXILE_AT_END_OF_YOUR_TURN:
                    game.doAction(new MagicAddTriggerAction(permanent, MagicAtEndOfTurnTrigger.ExileAtYourEnd(card.getController())));
                    break;
                case EXILE_AT_END_OF_TURN:
                    game.doAction(new MagicAddTriggerAction(permanent, MagicAtEndOfTurnTrigger.ExileAtEnd));
                    break;
                case EXILE_WHEN_LEAVES:
                    game.doAction(new MagicAddTriggerAction(permanent, MagicWhenLeavesPlayTrigger.Exile));
                    break;
                case SACRIFICE_AT_END_OF_TURN:
                    game.doAction(new MagicAddTriggerAction(permanent, MagicAtEndOfTurnTrigger.Sacrifice));
                    break;
                case ATTACKING:
                    permanent.setState(MagicPermanentState.Attacking);
                    break;
                case TAPPED:
                    permanent.setState(MagicPermanentState.Tapped);
                    break;
                case HASTE_UEOT:
                    game.doAction(new MagicGainAbilityAction(permanent, MagicAbility.Haste));
                    break;
                case HASTE:
                    game.doAction(new MagicGainAbilityAction(permanent, MagicAbility.Haste, MagicStatic.Forever));
                    break;
                case BLACK:
                    game.doAction(new MagicAddStaticAction(permanent, MagicStatic.Black));
                    break;
                case ZOMBIE:
                    game.doAction(new MagicAddStaticAction(permanent, MagicStatic.Zombie));
                    break;
            }
        }
        return permanent;
    }
}
