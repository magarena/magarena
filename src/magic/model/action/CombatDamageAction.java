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
public class CombatDamageAction extends MagicAction {

    private final MagicPlayer attackingPlayer;
    private final MagicPlayer defendingPlayer;
    private final boolean first;
    private boolean dealtDamage = false;

    public CombatDamageAction(final MagicPlayer attackingPlayer, final MagicPlayer defendingPlayer, final boolean first) {
        this.attackingPlayer=attackingPlayer;
        this.defendingPlayer=defendingPlayer;
        this.first = first;
    }

    private boolean dealsCombatDamage(final MagicPermanent creature) {
        return first ?
            creature.hasAbility(MagicAbility.FirstStrike) ||
            creature.hasAbility(MagicAbility.DoubleStrike)
            :
            creature.hasState(MagicPermanentState.DealtFirstStrike) == false ||
            creature.hasAbility(MagicAbility.DoubleStrike);
    }

    private void combatDamage(final MagicGame game, final MagicPlayer aAttackingPlayer, final MagicPlayer aDefendingPlayer) {

        final Collection<MagicDamage> combatDamage=new ArrayList<MagicDamage>();

        // Determine all combat damage that must be dealt.
        for (final MagicPermanent attacker : aAttackingPlayer.getPermanents()) {

            if (attacker.isAttacking()) {
                // Let blockers deal damage first.
                for (final MagicPermanent blocker : attacker.getBlockingCreatures()) {

                    // Checks if blocker deals first strike or regular combat damage.
                    if (dealsCombatDamage(blocker)) {
                        final int power=blocker.getPower();
                        // Checks if blocker has power > 0.
                        if (power>0) {
                            combatDamage.add(MagicDamage.Combat(blocker,attacker,power));
                        }
                        if (first) {
                            game.doAction(ChangeStateAction.Set(blocker, MagicPermanentState.DealtFirstStrike));
                        }
                    }
                }

                // Check if attacker deals first strike or regular combat damage.
                if (dealsCombatDamage(attacker)) {
                    // Checks if attacker has power > 0.
                    int power=attacker.getPower();
                    if (power>0 && !attacker.hasState(MagicPermanentState.NoCombatDamage)) {
                        if (attacker.hasState(MagicPermanentState.Blocked)) {
                            // Determine what damage must be dealt to each blocker.
                            final boolean deathtouch = attacker.hasAbility(MagicAbility.Deathtouch);
                            final MagicPermanentList blockers=attacker.getBlockingCreatures();
                            final int[] attackerDamage=new int[blockers.size()];
                            for (int index=0;power>0&&index<attackerDamage.length;index++) {

                                final MagicPermanent blocker=blockers.get(index);
                                final int toughness=blocker.getToughness();
                                attackerDamage[index]=Math.min(deathtouch?1:power,blocker.getLethalDamage(toughness));
                                power-=attackerDamage[index];
                            }

                            // Check what to do with the remaining damage from attacker.
                            if (power>0) {
                                if (attacker.hasAbility(MagicAbility.Trample)) {
                                    combatDamage.add(MagicDamage.Combat(attacker,aDefendingPlayer,power));
                                } else if (attackerDamage.length>0) {
                                    attackerDamage[0]+=power;
                                }
                            }

                            // Deal damage from attacker to blockers.
                            for (int index=0;index<attackerDamage.length;index++) {

                                final int amount=attackerDamage[index];
                                if (amount>0) {
                                    combatDamage.add(MagicDamage.Combat(attacker,blockers.get(index),amount));
                                }
                            }
                        } else {
                            // Deal all damage to defending player.
                            combatDamage.add(MagicDamage.Combat(attacker,aDefendingPlayer,power));
                        }
                    }
                    if (first) {
                        game.doAction(ChangeStateAction.Set(attacker, MagicPermanentState.DealtFirstStrike));
                    }
                }
            }
        }

        // Deal combat damage.
        if (!combatDamage.isEmpty()) {
            for (final MagicDamage damage: combatDamage) {
                game.doAction(new DealDamageAction(damage));
            }
            dealtDamage = true;
            game.setStateCheckRequired();
        }
    }

    public boolean dealtDamage() {
        return dealtDamage;
    }

    @Override
    public void doAction(final MagicGame game) {
        combatDamage(game,attackingPlayer,defendingPlayer);
    }

    @Override
    public void undoAction(final MagicGame game) {

    }
}
