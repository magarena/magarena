package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Simplification of the rules:
 * - the first blocker gets all remaining damage when the attacker does not have trample.
 * - when the attacker has deathtouch, lethal damage for each blocker is 1.
 */
public class MagicCombatDamageAction extends MagicAction {
	
	private final MagicPlayer attackingPlayer;
	private final MagicPlayer defendingPlayer;
    private final boolean first;
	
	public MagicCombatDamageAction(
            final MagicPlayer attackingPlayer,
            final MagicPlayer defendingPlayer, 
            final boolean first) {
		this.attackingPlayer=attackingPlayer;
		this.defendingPlayer=defendingPlayer;
        this.first = first;
	}

	private boolean dealsCombatDamage(final long flags) {
        return first ? MagicAbility.FirstStrike.hasAbility(flags) || MagicAbility.DoubleStrike.hasAbility(flags) :
                      !MagicAbility.FirstStrike.hasAbility(flags) || MagicAbility.DoubleStrike.hasAbility(flags);
	}
	
	private void combatDamage(
            final MagicGame game,
            final MagicPlayer aAttackingPlayer,
            final MagicPlayer aDefendingPlayer) {

		final Collection<MagicDamage> combatDamage=new ArrayList<MagicDamage>();

		// Determine all combat damage that must be dealt.
		for (final MagicPermanent attacker : aAttackingPlayer.getPermanents()) {
			
			if (attacker.isAttacking()) {
				// Let blockers deal damage first.
				for (final MagicPermanent blocker : attacker.getBlockingCreatures()) {

					// Checks if blocker deals first strike or regular combat damage.
					final long flags=blocker.getAllAbilityFlags(game);
					if (dealsCombatDamage(flags)) {
						final int power=blocker.getPower(game);
						// Checks if blocker has power > 0.
						if (power>0) {
							combatDamage.add(new MagicDamage(blocker,attacker,power,true));
						}
					}
				}

				// Check if attacker deals first strike or regular combat damage.
				final long flags=attacker.getAllAbilityFlags(game);
				if (dealsCombatDamage(flags)) {
					// Checks if attacker has power > 0.
					int power=attacker.getPower(game);
					if (power>0) {
						if (attacker.hasState(MagicPermanentState.Blocked)) {
							// Determine what damage must be dealt to each blocker.
							final boolean deathtouch=MagicAbility.Deathtouch.hasAbility(flags);
							final MagicPermanentList blockers=attacker.getBlockingCreatures();
							final int attackerDamage[]=new int[blockers.size()];
							for (int index=0;power>0&&index<attackerDamage.length;index++) {
								
								final MagicPermanent blocker=blockers.get(index);
								final int toughness=blocker.getToughness(game);
								attackerDamage[index]=Math.min(deathtouch?1:power,blocker.getLethalDamage(toughness));
								power-=attackerDamage[index];								
							}
							
							// Check what to do with the remaining damage from attacker.
							if (power>0) {
								if (MagicAbility.Trample.hasAbility(flags)) {
									combatDamage.add(new MagicDamage(attacker,aDefendingPlayer,power,true));
								} else if (attackerDamage.length>0) {
									attackerDamage[0]+=power;
								}
							}
							
							// Deal damage from attacker to blockers.
							for (int index=0;index<attackerDamage.length;index++) {
	
								final int amount=attackerDamage[index];
								if (amount>0) {
									combatDamage.add(new MagicDamage(attacker,blockers.get(index),amount,true));
								}
							}
						} else {
							// Deal all damage to defending player.
							combatDamage.add(new MagicDamage(attacker,aDefendingPlayer,power,true));
						}
					}
				}
			}
		}

		// Deal combat damage.
		if (!combatDamage.isEmpty()) {
			for (final MagicDamage damage: combatDamage) {
				game.doAction(new MagicDealDamageAction(damage));
			}
			game.setStateCheckRequired();
		}
	}

	@Override
	public void doAction(final MagicGame game) {
		combatDamage(game,attackingPlayer,defendingPlayer);
	}

	@Override
	public void undoAction(final MagicGame game) {
		
	}
}
