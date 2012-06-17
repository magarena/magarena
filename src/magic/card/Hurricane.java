package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Hurricane {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            final MagicPlayer player=cardOnStack.getController();
            final MagicCard card=cardOnStack.getCard();
            return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{cardOnStack,amount},
                    this,
                    card + " deals "+amount+" damage to each creature with flying and each player.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
            game.doAction(new MagicMoveCardAction(cardOnStack));
            final MagicSource source=cardOnStack.getCard();
            final int amount=(Integer)data[1];
            final Collection<MagicTarget> targets=
                game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITH_FLYING);
            for (final MagicTarget target : targets) {
                final MagicDamage damage=new MagicDamage(source,target,amount,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getPlayers()) {
                final MagicDamage damage=new MagicDamage(source,player,amount,false);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
}
