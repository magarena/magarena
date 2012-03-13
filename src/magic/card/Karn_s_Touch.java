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

    static final MagicStatic PT = Karn__Silver_Golem.PT;
    static final MagicStatic ST = Karn__Silver_Golem.ST;
    static final MagicTargetPicker<MagicPermanent> TP = Karn__Silver_Golem.TP;
	
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
