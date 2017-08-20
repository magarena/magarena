package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.choice.MagicTargetChoice;
import magic.model.mstatic.MagicPermanentStatic;

import java.util.Collection;
import java.util.Collections;

/**
 * Unattaches attachable from currently attached creature.
 * Attaches attachable to new creature when not MagicPermanent.NONE.
 */
public class AttachAction extends MagicAction {

    private final MagicPermanent attachable;
    private final MagicPermanent creature;
    private MagicPermanent oldAttachedCreature = MagicPermanent.NONE;
    private Collection<MagicPermanentStatic> oldStatics = Collections.emptyList();
    private boolean validCreature;

    public AttachAction(final MagicPermanent aAttachable,final MagicPermanent aCreature) {
        attachable = aAttachable;
        creature = aCreature;
    }

    private MagicPermanent getAttached() {
        return attachable.isEquipment() ?
            attachable.getEquippedCreature() :
            attachable.getEnchantedPermanent();
    }

    private void detach(final MagicPermanent old) {
        if (attachable.isEquipment()) {
            old.removeEquipment(attachable);
        } else {
            old.removeAura(attachable);
        }
    }

    private void attach(final MagicPermanent perm) {
        if (attachable.isEquipment()) {
            perm.addEquipment(attachable);
            attachable.setEquippedCreature(perm);
        } else {
            perm.addAura(attachable);
            attachable.setEnchantedPermanent(perm);
        }
    }

    @Override
    public boolean isLegal(final MagicGame game) {
        if (attachable.isValid() == false) {
            return false;
        }

        if (attachable.isEquipment() && creature.isValid() && !creature.isCreature()) {
            return false;
        }

        if (attachable.isAura() && creature.isValid()) {
            final MagicTargetChoice tchoice = new MagicTargetChoice(attachable.getAuraTargetChoice(), false);
            if (game.isLegalTarget(attachable.getController(),attachable,tchoice,creature) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void doAction(final MagicGame game) {
        int score = ArtificialScoringSystem.getTurnScore(game);

        oldAttachedCreature = getAttached();
        if (oldAttachedCreature.isValid()) {
            score-=oldAttachedCreature.getScore();
            detach(oldAttachedCreature);
            score+=oldAttachedCreature.getScore();
            if (oldAttachedCreature.isFriend(attachable)) {
                // Prevent unnecessary equips.
                if (oldAttachedCreature == creature) {
                    score += ArtificialScoringSystem.UNNECESSARY_EQUIP_SCORE;
                } else {
                    score += ArtificialScoringSystem.UNEQUIP_SCORE;
                }
            } else {
                score = -score;
            }
        }

        validCreature = creature.isValid();
        if (validCreature) {
            score-=creature.getScore();

            attach(creature);

            //update the timestamp of the attachable's effects
            oldStatics = game.removeSelfStatics(attachable);
            game.addStatics(attachable);

            score+=creature.getScore();
        } else {
            attach(MagicPermanent.NONE);
        }

        setScore(attachable.getController(),score);
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        if (validCreature) {
            detach(creature);
            game.removeSelfStatics(attachable);
            game.addStatics(oldStatics);
        }

        attach(oldAttachedCreature);
    }
}
