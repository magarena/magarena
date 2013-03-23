package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Breath_of_Darigaaz {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    new MagicKickerChoice(MagicManaCost.create("{2}"),false),
                    this,
                    "SN deals 1 damage to each creature without flying and each player. " + 
                    "If SN was kicked$, it deals 4 damage to each creature without flying and each player instead.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final int amount = event.isKicked() ? 4 : 1;
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,amount);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPlayer player : game.getPlayers()) {
                final MagicDamage damage=new MagicDamage(event.getSource(),player,amount);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    };
}
