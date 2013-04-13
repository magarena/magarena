package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.mstatic.MagicPermanentStatic;
import magic.model.trigger.MagicPermanentTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class MagicRemoveFromPlayAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicLocationType toLocation;
    private boolean valid;
    
    public MagicRemoveFromPlayAction(final MagicPermanent permanent,final MagicLocationType toLocation) {
        this.permanent=permanent;
        this.toLocation=toLocation;
    }

    public boolean isValid() {
        return valid;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        final MagicPlayer controller=permanent.getController();
        
        // Check if this is still a valid action.
        valid=controller.controlsPermanent(permanent);
        if (!valid) {
            return;
        }
            
        final int score=permanent.getScore()+permanent.getStaticScore();

        // Execute trigger here so that full permanent state is preserved.
        if (toLocation==MagicLocationType.Graveyard) {
            game.executeTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,permanent);
            if (permanent.isCreature()) {
                game.setCreatureDiedThisTurn(true);
            }
        }
        
        game.executeTrigger(MagicTriggerType.WhenLeavesPlay,permanent);
        
        // Equipment
        if (permanent.getEquippedCreature().isValid()) {
            permanent.getEquippedCreature().removeEquipment(permanent);
        }
        for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
            equipment.setEquippedCreature(MagicPermanent.NONE);
        }

        // Aura
        if (permanent.getEnchantedCreature().isValid()) {
            permanent.getEnchantedCreature().removeAura(permanent);
        }
        for (final MagicPermanent aura : permanent.getAuraPermanents()) {
            aura.setEnchantedCreature(MagicPermanent.NONE);
        }
        
        // Soulbond
        if (permanent.getPairedCreature().isValid()) {
            game.doAction(new MagicSoulbondAction(permanent,permanent.getPairedCreature(),false));
        }

        game.doAction(new MagicRemoveFromCombatAction(permanent));

        controller.removePermanent(permanent);

        setScore(controller,permanent.getStaticScore()-score);
        
        game.doAction(new MagicMoveCardAction(permanent,toLocation));
        game.addDelayedAction(new MagicRemoveTriggersStaticsAction(permanent));
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {

        if (!valid) {
            return;
        }
        
        permanent.getController().addPermanent(permanent);
        
        // Equipment
        if (permanent.getEquippedCreature().isValid()) {
            permanent.getEquippedCreature().addEquipment(permanent);
        }
        for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
            equipment.setEquippedCreature(permanent);
        }
        
        // Aura
        if (permanent.getEnchantedCreature().isValid()) {
            permanent.getEnchantedCreature().addAura(permanent);
        }
        for (final MagicPermanent aura : permanent.getAuraPermanents()) {
            aura.setEnchantedCreature(permanent);
        }
    }

    @Override
    public String toString() {
        return super.toString()+" ("+permanent.getName()+','+permanent.getAuraPermanents().size()+')';
    }
}
