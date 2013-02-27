package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetPicker;

public class Karn__Silver_Golem {
    static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int cmc = permanent.getConvertedCost();
            pt.set(cmc,cmc);
        }
    };
    static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
        }
    };
    static final MagicTargetPicker<MagicPermanent> TP = new MagicTargetPicker<MagicPermanent>() {
        @Override
        protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            final MagicPowerToughness pt=permanent.getPowerToughness();
            final int power = permanent.getConvertedCost();
            final int toughness = permanent.getConvertedCost();
            final int score = (pt.power()-power)*2+(pt.toughness()-toughness);
            return permanent.getController() == player ? -score:score;
        }
    };
    
    public static final MagicPermanentActivation A =new MagicPermanentActivation(
            new MagicCondition[]{MagicConditionFactory.ManaCost("{1}")},
            new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}"))};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_NONCREATURE_ARTIFACT,
                TP,
                this,
                "Target noncreature artifact$ becomes an artifact creature with " + 
                "power and toughness each equal to its converted mana cost until end of turn"
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicBecomesCreatureAction(creature,PT,ST));
                }
            });
        }
    };
}
