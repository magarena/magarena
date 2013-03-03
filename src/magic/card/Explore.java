package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Explore {
    public static final MagicSpellCardEvent EL=new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may play an additional land this turn."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicAddStaticAction(
                new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
                    @Override
                    public void modGame(final MagicPermanent source, final MagicGame game) {
                        game.incMaxLand();
                    }
                    @Override
                    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                        return game.getTurnPlayer().getId() == event.getPlayer().getId();
                    }
                }
            ));
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    };
}    
