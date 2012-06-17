package magic.card;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicReturnExiledUntilThisLeavesPlayAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Admonition_Angel {
    public static final MagicWhenOtherComesIntoPlayTrigger T1 = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            final MagicPlayer player = permanent.getController();
            if (player == played.getController() && played.isLand()) {
                final MagicTargetFilter targetFilter = 
                        new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                        MagicTargetFilter.TARGET_NONLAND_PERMANENT,permanent);
                final MagicTargetChoice targetChoice = 
                        new MagicTargetChoice(
                        targetFilter,true,MagicTargetHint.None,"another target nonland permanent to exile");
                final MagicChoice mayChoice = 
                        new MagicMayChoice(
                        player + " may exile another target nonland permanent.",
                        targetChoice);
                return new MagicEvent(
                    permanent,
                    player,
                    mayChoice,
                    MagicExileTargetPicker.create(),
                    new Object[]{permanent},
                    this,
                    player + " may$ exile another target nonland permanent$.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
                        game.doAction(new MagicExileUntilThisLeavesPlayAction((MagicPermanent)data[0],target));
                    }
                });
            }
        }        
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
            if (permanent == data &&
                !permanent.getExiledCards().isEmpty()) {
                final MagicCardList clist = new MagicCardList(permanent.getExiledCards());
                return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        clist.size() > 1 ?
                                "Return exiled cards to the battlefield." :
                                "Return " + clist.get(0) + " to the battlefield.");
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = (MagicPermanent)data[0];
            game.doAction(new MagicReturnExiledUntilThisLeavesPlayAction(permanent,MagicLocationType.Play));
        }
    };
}
