package magic.card;

import magic.model.MagicColor;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicColorChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

import java.util.Collection;

public class Treva__the_Renewer {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent && 
                    damage.getTarget().isPlayer() && 
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}{W}")),
                        MagicColorChoice.MOST_INSTANCE
                    ),
                    this,
                    "PN may$ pay {2}{W}$. If you do, choose a color$. " + 
                    "PN gains 1 life for each permanent of that color."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                int life=0;
                final MagicPlayer player=event.getPlayer();
                final MagicColor color=event.getChosenColor();
                final Collection<MagicPermanent> targets=game.filterPermanents(player,MagicTargetFilter.TARGET_PERMANENT);
                for (final MagicPermanent permanent : targets) {
                    if (permanent.hasColor(color)) {
                        life++;
                    }
                }                
                if (life>0) {
                    game.doAction(new MagicChangeLifeAction(player,life));
                }
            }
        }        
    };
}
