package magic.model.event;

import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.MagicGame;
import magic.model.choice.MagicKickerChoice;
import magic.model.action.MagicSetKickerAction;

public class MagicKickerCost extends MagicAdditionalCost implements MagicEventAction {
    
    final MagicManaCost manaCost;
    
    public MagicKickerCost(final MagicManaCost aManaCost) {
        manaCost = aManaCost;
    }

    public MagicKickerCost(final String cost) {
        manaCost = MagicManaCost.create(cost);
    }
    
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicKickerChoice(manaCost),
            this,
            ""
        );
    }
        
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.payManaCost(game,event.getPlayer());
        // TODO: use action for reversibility
        game.doAction(new MagicSetKickerAction(event.getCard(), event.getKickerCount()));
    }
}
