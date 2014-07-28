package magic.model.action;

import com.sun.org.apache.bcel.internal.generic.NEW;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicAtEndOfCombatTrigger;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public enum MagicPlayMod implements MagicPermanentAction {
    EXILE_AT_END_OF_COMBAT() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddTriggerAction(perm, MagicAtEndOfCombatTrigger.Exile));
        }
    },
    EXILE_AT_END_OF_TURN() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddTriggerAction(perm, MagicAtEndOfTurnTrigger.ExileAtEnd));
        }
    },
    EXILE_AT_END_OF_YOUR_TURN() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            final MagicPlayer controller = perm.getController();
            game.doAction(new MagicAddTriggerAction(perm, MagicAtEndOfTurnTrigger.ExileAtYourEnd(controller)));
        }
    },
    EXILE_WHEN_LEAVES() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddTriggerAction(perm, MagicWhenLeavesPlayTrigger.Exile));
        }
    },
    SACRIFICE_AT_END_OF_TURN() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddTriggerAction(perm, MagicAtEndOfTurnTrigger.Sacrifice));
        }
    },
    ATTACKING() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Attacking);
        }
    },
    TAPPED() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Tapped);
        }
    },
    HASTE_UEOT() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicGainAbilityAction(perm, MagicAbility.Haste));
        }
    },
    HASTE() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicGainAbilityAction(perm, MagicAbility.Haste, MagicStatic.Forever));
        }
    },
    PERSIST() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.changeCounters(MagicCounterType.MinusOne,1);
        }
    },
    UNDYING() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.changeCounters(MagicCounterType.PlusOne,1);
        }
    },
    BLACK() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddStaticAction(perm, MagicStatic.Black));
        }
    },
    ZOMBIE() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddStaticAction(perm, MagicStatic.Zombie));
        }
    },
    NIGHTMARE() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddStaticAction(perm, MagicStatic.Nightmare));
        }
    },
    BESTOWED() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddStaticAction(perm, MagicStatic.Bestowed));
        }
    },
    FACE_DOWN() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddStaticAction(perm, MagicStatic.FaceDownPermanent));
        }
    },
    NONE() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
        }
    },
    ;
    
    public void doAction(final MagicPermanent perm) {
        doAction(perm.getGame(), perm);
    }

    abstract protected void doAction(final MagicGame game, final MagicPermanent perm);
}
