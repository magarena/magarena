package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Titanic_Ultimatum {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Until end of turn, creatures you control get +5/+5 and gain first strike, lifelink and trample.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final Collection<MagicPermanent> targets=game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,5,5));
                game.doAction(new MagicSetAbilityAction(
                    creature,
                    MagicAbility.FirstStrike,
                    MagicAbility.LifeLink,
                    MagicAbility.Trample
                ));
            }
        }
    };
}
