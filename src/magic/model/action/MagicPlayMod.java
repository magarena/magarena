package magic.model.action;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import magic.model.MagicAbility;
import magic.model.MagicAbilityList;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.ARG;
import magic.model.event.MagicMorphActivation;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.AtEndOfCombatTrigger;
import magic.model.trigger.AtEndOfTurnTrigger;
import magic.model.trigger.LeavesBattlefieldTrigger;

public enum MagicPlayMod implements MagicPermanentAction {
    EXILE_AT_END_OF_COMBAT("Exile (that|the) token at end of combat") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddTriggerAction(perm, AtEndOfCombatTrigger.Exile));
        }
    },
    EXILE_AT_END_OF_TURN("Exile (it|them|that token) at the beginning of the next end step") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddTriggerAction(perm, AtEndOfTurnTrigger.ExileAtEnd));
        }
    },
    EXILE_AT_END_OF_YOUR_TURN("Exile it at the beginning of your next end step") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            final MagicPlayer controller = perm.getController();
            game.doAction(new AddTriggerAction(perm, AtEndOfTurnTrigger.ExileAtYourEnd(controller)));
        }
    },
    EXILE_AT_END_OF_YOUR_TURN2("At the beginning of your next end step, exile it") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            EXILE_AT_END_OF_YOUR_TURN.doAction(game, perm);
        }
    },
    EXILE_WHEN_LEAVES("If it would leave the battlefield, exile it instead of putting it anywhere else") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddTriggerAction(perm, LeavesBattlefieldTrigger.Exile));
        }
    },
    SACRIFICE_AT_END_OF_TURN("Sacrifice (it|those tokens) at the beginning of the next end step") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddTriggerAction(perm, AtEndOfTurnTrigger.Sacrifice));
        }
    },
    SACRIFICE_AT_END_OF_COMBAT("Sacrifice (it|the token) at end of combat") {
      @Override
      protected void doAction(final MagicGame game, final MagicPermanent perm) {
          game.doAction(new AddTriggerAction(perm, AtEndOfCombatTrigger.Sacrifice));
      }
    },
    DESTROY_AT_END_OF_TURN("Destroy (it|those tokens) at the beginning of the next end step") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddTriggerAction(perm, AtEndOfTurnTrigger.Destroy));
        }
    },
    RETURN_AT_END_OF_TURN() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddTriggerAction(perm, AtEndOfTurnTrigger.Return));
        }
    },
    ATTACKING("attacking") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Attacking);
        }
    },
    TAPPED("tapped") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Tapped);
        }
    },
    TAPPED_AND_ATTACKING("tapped and attacking") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            TAPPED.doAction(game, perm);
            ATTACKING.doAction(game, perm);
        }
    },
    HASTE_UEOT("(it|that " + ARG.WORD1 + ") gains haste until end of turn") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new GainAbilityAction(perm, MagicAbility.Haste));
        }
    },
    HASTE("(it|that token) (gains|has) haste") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new GainAbilityAction(perm, MagicAbility.Haste, MagicStatic.Forever));
        }
    },
    PERSIST() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.changeCounters(MagicCounterType.MinusOne,1);
        }
    },
    UNDYING() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.changeCounters(MagicCounterType.PlusOne,1);
        }
    },
    DEATH_COUNTER() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.changeCounters(MagicCounterType.Death,1);
        }
    },
    ARTIFACT() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddStaticAction(perm, MagicStatic.Artifact));
        }
    },
    ZOMBIE() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddStaticAction(perm, MagicStatic.Zombie));
        }
    },
    SPIRIT() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddStaticAction(perm, MagicStatic.Spirit));
        }
    },
    BLACK_ZOMBIE("(it|that creature) is a black Zombie in addition to its other colors and types") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddStaticAction(perm, MagicStatic.AddBlack));
            game.doAction(new AddStaticAction(perm, MagicStatic.Zombie));
        }
    },
    BLACK_NIGHTMARE("It is black and is a Nightmare in addition to its other creature types") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddStaticAction(perm, MagicStatic.IsBlack));
            game.doAction(new AddStaticAction(perm, MagicStatic.Nightmare));
        }
    },
    BESTOWED() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            game.doAction(new AddStaticAction(perm, MagicStatic.Bestowed));
        }
    },
    MANIFEST() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            if (perm.isCreature() && perm.getCardDefinition().hasCost()) {
                final MagicAbilityList morphAct = new MagicAbilityList();
                morphAct.add(MagicMorphActivation.Manifest);
                game.doAction(new GainAbilityAction(perm, morphAct, MagicStatic.Forever));
            }
            perm.setState(MagicPermanentState.FaceDown);
            perm.setState(MagicPermanentState.Manifest);
        }
    },
    MORPH() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.FaceDown);
        }
    },
    FLIPPED("flipped") {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Flipped);
        }
    },
    TRANSFORMED() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
            perm.setState(MagicPermanentState.Transformed);
        }
    },
    NONE() {
        @Override
        protected void doAction(final MagicGame game, final MagicPermanent perm) {
        }
    },
    ;

    private final Pattern pattern;

    private MagicPlayMod(final String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    private MagicPlayMod() {
        this("NONE");
    }

    @Override
    public void doAction(final MagicPermanent perm) {
        doAction(perm.getGame(), perm);
    }

    abstract protected void doAction(final MagicGame game, final MagicPermanent perm);

    public static MagicPlayMod getPlayMod(final String name) {
        for (final MagicPlayMod mod : values()) {
            if (mod.pattern.matcher(name).matches()) {
                return mod;
            }
        }
        throw new RuntimeException("unknown play mod \"" + name + "\"");
    }

    public static List<MagicPlayMod> build(final String text) {
        final String[] tokens = text != null ? text.split("\\. ") : new String[0];
        final List<MagicPlayMod> mods = new LinkedList<>();
        for (final String name : tokens) {
            mods.add(MagicPlayMod.getPlayMod(name));
        }
        return mods;
    }
}
