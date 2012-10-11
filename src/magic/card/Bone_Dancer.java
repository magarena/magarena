package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksUnblockedTrigger;

import java.util.List;

public class Bone_Dancer {
    public static final MagicWhenAttacksUnblockedTrigger T = new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may$ put the top creature card of " +
                    "opponent's graveyard onto the " +
                    "battlefield under his or her control."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final MagicPlayer opponent = event.getPlayer().getOpponent();
                final List<MagicCard> targets =
                        game.filterCards(opponent,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
                if (targets.size() > 0) {
                    final MagicCard card = targets.get(targets.size()-1);
                    game.doAction(new MagicReanimateAction(
                            event.getPlayer(),
                            card,
                            MagicPlayCardAction.NONE));
                }
                game.doAction(new MagicChangeStateAction(
                        event.getPermanent(),
                        MagicPermanentState.NoCombatDamage,true));
            }
        }
    };
}
