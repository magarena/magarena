package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.stack.MagicCardOnStack;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetPicker;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Karn_s_Touch {

    private static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int cmc = permanent.getCardDefinition().getConvertedCost();
			pt.set(cmc,cmc);
		}
    };
    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Artifact.getMask()|MagicType.Creature.getMask();
		}
	};
    private static final MagicTargetPicker<MagicPermanent> TP = new MagicTargetPicker<MagicPermanent>() {
        @Override
    	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
            final MagicPowerToughness pt=permanent.getPowerToughness(game);
            final int power = permanent.getCardDefinition().getConvertedCost();
            final int toughness = permanent.getCardDefinition().getConvertedCost();
            int score=(pt.power()-power)*2+(pt.toughness()-toughness);
            return permanent.getController()==player?-score:score;
        }
    };
	
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.POS_TARGET_NONCREATURE_ARTIFACT,
                    TP,
				    new Object[]{cardOnStack,player},this,
				    "Target noncreature artifact$ becomes an artifact creature with " + 
                    "power and toughness each equal to its converted mana cost until end of turn");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
			        game.doAction(new MagicBecomesCreatureAction(creature,PT,ST));
                }
			});
		}
	};
}
