package magic.model.action;

import java.util.List;
import java.util.Collections;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicStatic;

public class MagicPlayCardAction extends MagicPutIntoPlayAction {

    private final MagicCard card;
    private final MagicPlayer controller;
    private final List<MagicPlayMod> modifications;

    public MagicPlayCardAction(final MagicCard aCard, final MagicPlayer aController,final List<MagicPlayMod> aModifications) {
        card = aCard;
        controller = aController;
        modifications = aModifications;
    }
    
    public MagicPlayCardAction(final MagicCard card, final MagicPlayer player) {
        this(card, player, Collections.<MagicPlayMod>emptyList());
    }
    
    public MagicPlayCardAction(final MagicCard card, final List<MagicPlayMod> modifications) {
        this(card, card.getController(), modifications);
    }
    
    public MagicPlayCardAction(final MagicCard card) {
        this(card, card.getController(), Collections.<MagicPlayMod>emptyList());
    }

    @Override
    protected MagicPermanent createPermanent(final MagicGame game) {
        final MagicPermanent permanent=game.createPermanent(card,controller);
        for (final MagicPlayMod modification : modifications) {
            switch (modification) {
                case UNDYING:
                    permanent.changeCounters(MagicCounterType.PlusOne,1);
                    break;
                case PERSIST:
                    permanent.changeCounters(MagicCounterType.MinusOne,1);
                    break;
                case EXILE_AT_END_OF_COMBAT:
                    permanent.setState(MagicPermanentState.ExileAtEndOfCombat);
                    break;
                case EXILE_AT_END_OF_TURN:
                    permanent.setState(MagicPermanentState.ExileAtEndOfTurn);
                    break;
                case EXILE_AT_END_OF_YOUR_TURN:
                    permanent.setState(MagicPermanentState.ExileAtEndOfYourTurn);
                    break;
                case SACRIFICE_AT_END_OF_TURN:
                    permanent.setState(MagicPermanentState.SacrificeAtEndOfTurn);
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
