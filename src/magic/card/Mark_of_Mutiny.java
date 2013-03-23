package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicExileTargetPicker;

public class Mark_of_Mutiny {
    public static final MagicSpellCardEvent SOR=new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicExileTargetPicker.create(),
                    this,
                    "Gain control of target creature$ until end of turn. Put a +1/+1 counter on it and untap it. " +
                    "That creature gains haste until end of turn.");
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainControlAction(event.getPlayer(),creature,MagicStatic.UntilEOT));
                    game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1,true));
                    game.doAction(new MagicUntapAction(creature));
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
                }
            });
        }
    };
}
