package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicCDA;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Kodama_of_the_Center_Tree {
    public static final MagicCDA CDA = new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPowerToughness pt) {
            final int amount = game.filterPermanents(player,MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL).size();
            pt.set(amount, amount);
        }
    };
    
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            if (triggerData.fromLocation == MagicLocationType.Play) {
                final MagicPlayer player = permanent.getController();
                final int cmc = game.filterPermanents(player,
                        MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL).size()+1;
                final MagicTargetFilter<MagicTarget> targetFilter =
                        new MagicTargetFilter.MagicCMCTargetFilter<MagicTarget>(
                            MagicTargetFilter.TARGET_SPIRIT_CARD_FROM_GRAVEYARD,
                            MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL,
                            cmc
                        );
                final MagicTargetChoice targetChoice = 
                        new MagicTargetChoice(
                        targetFilter,false,MagicTargetHint.None,
                        "a Spirit card from your graveyard)");
                return new MagicEvent(
                        permanent,
                        new MagicMayChoice(targetChoice),
                        new MagicGraveyardTargetPicker(false),
                        this,
                        "PN may$ return target Spirit card$ with " +
                        "converted mana cost " + cmc + " or less " +
                        "from his or her graveyard to his or her hand.");
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(
                                card,
                                MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(
                                card,
                                MagicLocationType.Graveyard,
                                MagicLocationType.OwnersHand));
                    }
                });
            }
        }
    };
}
