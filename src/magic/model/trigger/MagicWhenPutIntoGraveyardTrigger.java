package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicCardDefinition;
import magic.model.event.MagicEvent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;

public abstract class MagicWhenPutIntoGraveyardTrigger extends MagicTrigger<MagicGraveyardTriggerData> {
    public MagicWhenPutIntoGraveyardTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenPutIntoGraveyardTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenPutIntoGraveyard;
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
    
    public static final MagicWhenPutIntoGraveyardTrigger RecoverGraveyard = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            final MagicPlayer owner = triggerData.card.getOwner();
            return new MagicEvent(
                //source may be permanent if on battlefield or card (exile, hand)
                permanent.isValid() ? permanent : triggerData.card,
                owner,
                this,
                "PN shuffles his or her graveyard into his or her library."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            for (final MagicCard card : graveyard) {
                game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.OwnersLibrary));            
            }
        }
    };
}
