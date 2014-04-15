package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public class MagicAddCantripTriggerAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicTrigger<?> trigger;
    private MagicPermanentTrigger permanentTrigger;

    public MagicAddCantripTriggerAction(final MagicSource source, final MagicPlayer player) {
        this.permanent=MagicPermanent.NONE;
        this.trigger=cantrip(source, player);
    }
    
    public MagicAtUpkeepTrigger cantrip(final MagicSource source, final MagicPlayer player) {
        return new MagicAtUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                game.addDelayedAction(new MagicRemoveTriggerAction(this));
                return new MagicEvent(
                    game.createDelayedSource(source, player),
                    this,         
                    "PN draws a card"
                );
            }
        
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }; 
    }
    
    @Override
    public void doAction(final MagicGame game) {
        permanentTrigger=game.addTrigger(permanent,trigger);
    }

    @Override
    public void undoAction(final MagicGame game) {
        game.removeTrigger(permanentTrigger);
    }
    
    @Override                                                                                                                                         
    public String toString() {
        return getClass().getSimpleName()+" ("+permanent+','+trigger+')';
    }
}
