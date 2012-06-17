package magic.card;

import java.util.EnumSet;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Angel_s_Tomb {

    private static final MagicStatic PT = new MagicStatic(
            MagicLayer.SetPT,
            MagicStatic.UntilEOT) {

        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            pt.set(3, 3);
        }
    };
    private static final MagicStatic AB = new MagicStatic(
            MagicLayer.Ability,
            MagicStatic.UntilEOT) {

        @Override
        public long getAbilityFlags(
                final MagicGame game,
                final MagicPermanent permanent,
                final long flags) {
            return flags | MagicAbility.Flying.getMask();
        }
    };
    private static final MagicStatic ST = new MagicStatic(
            MagicLayer.Type,
            MagicStatic.UntilEOT) {

        @Override
        public void modSubTypeFlags(
                final MagicPermanent permanent,
                final EnumSet<MagicSubType> flags) {
            flags.add(MagicSubType.Angel);
        }

        @Override
        public int getTypeFlags(final MagicPermanent permanent, final int flags) {
            return flags | MagicType.Creature.getMask();
        }
    };
    private static final MagicStatic C = new MagicStatic(
            MagicLayer.Color,
            MagicStatic.UntilEOT) {

        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return MagicColor.White.getMask();
        }
    };
    
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (otherPermanent.getController() == player &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                            player + " may have " + permanent + " become a 3/3 white " +
                            "Angel artifact creature with flying until end of turn.",
                            MagicSimpleMayChoice.BECOME_CREATURE,
                            0,
                            MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{permanent},
                        this,
                        player + " may$ have " + permanent + " become a 3/3 white " +
                        "Angel artifact creature with flying until end of turn."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicBecomesCreatureAction(
                        (MagicPermanent)data[0],
                        PT,
                        AB,
                        ST,
                        C));
            }            
        }        
    };
}
