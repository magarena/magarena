package magic.model.action;

import java.util.List;
import java.util.LinkedList;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicAbility;
import magic.model.MagicAbilityList;
import magic.model.MagicCounterType;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicAtEndOfCombatTrigger;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;
import magic.model.event.MagicEvent;
import magic.model.event.MagicMorphActivation;
import magic.model.event.MagicMatchedCostEvent;
import magic.model.event.MagicPayManaCostEvent;

public enum MagicPlayMod implements MagicPermanentAction {
    EXILE_AT_END_OF_COMBAT("Exile that token at end of combat") {
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
    RETURN_AT_END_OF_TURN() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new MagicAddTriggerAction(perm, MagicAtEndOfTurnTrigger.Return));
        }
    },
    ATTACKING("attacking") {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Attacking);
        }
    },
    TAPPED("tapped") {
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
    MANIFEST() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            if (perm.isCreature()) {
                final MagicManaCost manaCost = perm.getCardDefinition().getCost();
                final MagicAbilityList morphAct = new MagicAbilityList();
                final List<MagicMatchedCostEvent> cost = new LinkedList<>();
                cost.add(new MagicMatchedCostEvent() {
                    public MagicEvent getEvent(final MagicSource source) {
                        return new MagicPayManaCostEvent(source, manaCost); 
                    }
                    public boolean isIndependent() {
                        return true;
                    }
                });
                morphAct.add(new MagicMorphActivation(cost));
                game.doAction(new MagicGainAbilityAction(perm, morphAct, MagicStatic.Forever));
            }
            perm.setState(MagicPermanentState.FaceDown);
        }
    },
    MORPH() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.FaceDown);
        }
    },
    FLIPPED() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Flipped);
        }
    },
    TRANSFORMED() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Transformed);
        }
    },
    NONE() {
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
        }
    },
    ;

    final String text;

    private MagicPlayMod(final String name) {
        text = name;
    }

    private MagicPlayMod() {
        this("");
    }

    @Override
    public String toString() {
        return text;
    }

    public void doAction(final MagicPermanent perm) {
        doAction(perm.getGame(), perm);
    }

    abstract protected void doAction(final MagicGame game, final MagicPermanent perm);
    
    public static MagicPlayMod getPlayMod(final String name) {
        for (final MagicPlayMod mod : values()) {
            if (mod.toString().equalsIgnoreCase(name)) {
                return mod;
            }
        }
        throw new RuntimeException("unknown play mod \"" + name + "\"");
    }
    
    public static List<MagicPlayMod> build(final String text) {
        final String[] tokens = text != null ? text.split("\\. |, | and ") : new String[0];
        final List<MagicPlayMod> mods = new LinkedList<>();
        for (final String name : tokens) {
            mods.add(MagicPlayMod.getPlayMod(name));
        }
        return mods;
    }
}
