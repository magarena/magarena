package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

public class RemoveFromPlayAction extends MagicAction {

    private final MagicPermanent permanent;
    private MagicLocationType toLocation;
    private boolean valid;

    public RemoveFromPlayAction(final MagicPermanent aPermanent,final MagicLocationType aToLocation) {
        permanent  = aPermanent;
        toLocation = aToLocation;
    }

    public boolean isValid() {
        return valid;
    }
    
    public boolean isPermanent(final MagicPermanent aPermanent) {
        return permanent == aPermanent;
    }
    
    public MagicPermanent getPermanent() {
        return permanent;
    }

    public MagicLocationType getToLocation() {
        return toLocation;
    }

    public void setToLocation(final MagicLocationType aToLocation) {
        toLocation = aToLocation;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicPlayer controller=permanent.getController();

        // Check if this is still a valid action.
        valid = permanent.isValid();
        if (!valid) {
            return;
        }

        final int score=permanent.getScore()+permanent.getStaticScore();

        // Execute trigger here so that full permanent state is preserved.
        game.executeTrigger(MagicTriggerType.WhenLeavesPlay, this);

        if (toLocation==MagicLocationType.Graveyard) {
            game.executeTrigger(MagicTriggerType.WhenOtherDies,permanent);
            if (permanent.isCreature()) {
                game.setCreatureDiedThisTurn(true);
            }
        }

        // Equipment
        if (permanent.getEquippedCreature().isValid()) {
            permanent.getEquippedCreature().removeEquipment(permanent);
        }
        for (final MagicPermanent equipment : permanent.getEquipmentPermanents()) {
            equipment.setEquippedCreature(MagicPermanent.NONE);
        }

        // Aura
        if (permanent.getEnchantedPermanent().isValid()) {
            permanent.getEnchantedPermanent().removeAura(permanent);
        }
        for (final MagicPermanent aura : permanent.getAuraPermanents()) {
            aura.setEnchantedPermanent(MagicPermanent.NONE);
        }

        // Soulbond
        if (permanent.getPairedCreature().isValid()) {
            game.doAction(new SoulbondAction(permanent,permanent.getPairedCreature(),false));
        }

        game.doAction(new RemoveFromCombatAction(permanent));

        controller.removePermanent(permanent);

        setScore(controller,permanent.getStaticScore()-score);

        game.doAction(new MoveCardAction(permanent,toLocation));
        game.addDelayedAction(new RemoveTriggersStaticsAction(permanent));
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
        if (permanent.getEnchantedPermanent().isValid()) {
            permanent.getEnchantedPermanent().addAura(permanent);
        }
        for (final MagicPermanent aura : permanent.getAuraPermanents()) {
            aura.setEnchantedPermanent(permanent);
        }
    }

    @Override
    public String toString() {
        return super.toString()+" ("+permanent.getName()+','+permanent.getAuraPermanents().size()+')';
    }
}
