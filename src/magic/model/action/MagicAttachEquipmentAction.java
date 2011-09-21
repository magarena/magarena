package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicPermanentStatic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Unattaches equipment from currently equipped creature.
 * Attaches equipment to new creature when not MagicPermanent.NONE.
 */
public class MagicAttachEquipmentAction extends MagicAction {

	private final MagicPermanent equipment;
	private final MagicPermanent creature;
	private MagicPermanent oldEquippedCreature = MagicPermanent.NONE;
    private Collection<MagicPermanentStatic> oldStatics = Collections.emptyList();
	private boolean validEquipment;
	private boolean validCreature;
	
	public MagicAttachEquipmentAction(final MagicPermanent equipment,final MagicPermanent creature) {
		this.equipment=equipment;
		this.creature=creature;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		validEquipment=equipment.getController().controlsPermanent(equipment);
		if (!validEquipment) {
			return;
		}
		int score=ArtificialScoringSystem.getTurnScore(game);
		oldEquippedCreature=equipment.getEquippedCreature();
		if (oldEquippedCreature.isValid()) {
			score-=oldEquippedCreature.getScore(game);
			oldEquippedCreature.removeEquipment(equipment);
			score+=oldEquippedCreature.getScore(game);
			if (oldEquippedCreature.getController()==equipment.getController()) {
				// Prevent unnecessary equips.
				if (oldEquippedCreature==creature) {
					score+=ArtificialScoringSystem.UNNECESSARY_EQUIP_SCORE;
				} else {
					score+=ArtificialScoringSystem.UNEQUIP_SCORE;
				}
			} else {
				score=-score;
			}
		}
		validCreature = creature.isValid() && creature.getController().controlsPermanent(creature);
		if (validCreature) {
			score-=creature.getScore(game);

			creature.addEquipment(equipment);
			equipment.setEquippedCreature(creature);

            //update the timestamp of the equipment's effects
            oldStatics = game.removeStatics(equipment);
            game.addStatic(equipment);

			score+=creature.getScore(game);
		} else {
			equipment.setEquippedCreature(MagicPermanent.NONE);
		}
		setScore(equipment.getController(),score);		
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (!validEquipment) {
			return;
		}

        if (validCreature) {
            creature.removeEquipment(equipment);
            game.removeStatics(equipment);
            game.addStatics(oldStatics);
        }

        equipment.setEquippedCreature(oldEquippedCreature);

        if (oldEquippedCreature.isValid()) {
            oldEquippedCreature.addEquipment(equipment);
        }
	}
}
