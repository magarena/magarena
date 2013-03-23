package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Tuktuk_Scrapper {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.NEG_TARGET_ARTIFACT
                    ),
                    new MagicDestroyTargetPicker(false),
                    this,
                    "PN may$ destroy target artifact$."
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
                        game.doAction(new MagicDestroyAction(target));
                        final MagicCard card = target.getCard();
                        final MagicPlayer player = event.getPlayer();
                        // only deal damage when the target is destroyed
                        if (card.getOwner().getGraveyard().contains(card)
                            ||
                            (card.isToken() &&
                            !card.getOwner().getPermanents().contains(target))) {
                            final int amount =
                                    player.getNrOfPermanentsWithSubType(MagicSubType.Ally);
                            if (amount > 0) {
                                final MagicDamage damage = new MagicDamage(
                                    event.getPermanent(),
                                    card.getOwner(),
                                    amount
                                );
                                game.doAction(new MagicDealDamageAction(damage));
                            }
                        }
                    }
                });
            }            
        }        
    };
}
