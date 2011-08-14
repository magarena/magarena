package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.score.*;

import java.util.*;

public class MagicDeclareBlockersResultBuilder {

	private static final Collection<Object> EMPTY_RESULT = 
        Collections.<Object>singletonList(new MagicDeclareBlockersResult(0,0));
	private static final int MAX_RESULTS=12;
	private static final int MAX_ATTACKERS=3;
	private static final int MAX_TURN=1;

	private final MagicGame game;
	private final MagicPlayer attackingPlayer;
	private final MagicPlayer defendingPlayer;
	private final boolean fast;
	private MagicScoreRanking results;
	private MagicDeclareBlockersResult result;
	private MagicCombatScore combatScore;
	private MagicCombatCreature attackers[];
	private Set<MagicCombatCreature> blockers;
	private int position;
		
	public MagicDeclareBlockersResultBuilder(final MagicGame game,final MagicPlayer defendingPlayer,final boolean fast) {
		this.game=game;
		this.defendingPlayer=defendingPlayer;
		this.attackingPlayer=game.getOpponent(defendingPlayer);
		this.fast=fast;
	}
	
    private void buildBlockersFast() {
        System.err.println("Running randomized blocking algorithm");
        
        //generate basic blocks 
        //buildBasicBlocks(0);
        
        //sample 100000 random blocks 
        final magic.MersenneTwisterFast rng = new magic.MersenneTwisterFast(attackers.length + blockers.size());
        for (int i = 0; i < 100000; i++) {
            Map<Integer, List<MagicCombatCreature>> block = 
                new HashMap<Integer, List<MagicCombatCreature>>();
            for (int j = 0; j < attackers.length; j++) {
                block.put(j, new ArrayList<MagicCombatCreature>(5));
                block.get(j).add(attackers[j]);
            }
            for (final MagicCombatCreature blocker : blockers) {
                //determine attackers it can block
                List<Integer> choices = new ArrayList<Integer>();
                for (int j = 0; j < attackers.length; j++) {
                    if (Arrays.asList(attackers[j].candidateBlockers).contains(blocker)) {
                        choices.add(j);
                    }
                }
                //choose one of the attackers or don't block
                final int idx = rng.nextInt(choices.size() + 1);
                if (idx < choices.size()) {
                    block.get(choices.get(idx)).add(blocker);
                }
            }

            //convert block to a MagicDeclareBlockersResult object
            result.clear();
            for (int j = 0; j < attackers.length; j++) {
                result.add(block.get(j).toArray(new MagicCombatCreature[0]));
            }

            //score result
            final int score=combatScore.getScore(result);
			if (results.addScore(score)) {
				results.addScoreResult(new MagicDeclareBlockersResult(result,position++,score));
			}
        }
	}
	
    private void buildBasicBlocks(final int index) {
		// A new result is found.
		if (index==attackers.length) {
			final int score=combatScore.getScore(result);
			if (results.addScore(score)) {
				results.addScoreResult(new MagicDeclareBlockersResult(result,position++,score));
			}
			return;
		}
		
		// Get the remaining candidate blockers.
		final MagicCombatCreature attacker=attackers[index];
		final MagicCombatCreature candidateBlockers[]=new MagicCombatCreature[attacker.candidateBlockers.length];
		int blockersSize=0;
		for (MagicCombatCreature blocker : attacker.candidateBlockers) {
			if (blockers.contains(blocker)) {
				candidateBlockers[blockersSize++]=blocker;
			}
		}
		
		// No blockers.
		result.addLast(new MagicCombatCreature[]{attacker});
		buildAttacker(index+1);
		result.removeLast();
		if (blockersSize == 0) {
			return;
		}
		
		// One blocker.
		if (blockersSize == 1) {
			final MagicCombatCreature blocker = candidateBlockers[0];
			blockers.remove(blocker);
			result.addLast(new MagicCombatCreature[]{attacker,blocker});
			buildAttacker(index+1);
			result.removeLast();
			blockers.add(blocker);
			return;
		}
		
        // Single blocker which does not deal lethal damage to the attacker.
        // Not sufficient: might want to chump block with multiple blockers to
        // survive the attack or damage the attackers enough to finish it off
        // with direct damage
		int lethalDamage = attacker.lethalDamage;
		for (int blockerIndex = 0; blockerIndex < blockersSize; blockerIndex++) {
			final MagicCombatCreature blocker=candidateBlockers[blockerIndex];
			if (blocker.power < lethalDamage) {
				blockers.remove(blocker);
				result.addLast(new MagicCombatCreature[]{attacker,blocker});
				buildAttacker(index+1);
				result.removeLast();
				blockers.add(blocker);
			}
		}
    }
		
	private void buildAttacker(final int index) {

		// A new result is found.
		if (index==attackers.length) {
			final int score=combatScore.getScore(result);
			if (results.addScore(score)) {
				results.addScoreResult(new MagicDeclareBlockersResult(result,position++,score));
			}
			return;
		}
		
		// Get the remaining candidate blockers.
		final MagicCombatCreature attacker=attackers[index];
		final MagicCombatCreature candidateBlockers[]=new MagicCombatCreature[attacker.candidateBlockers.length];
		int blockersSize=0;
		for (MagicCombatCreature blocker : attacker.candidateBlockers) {
			if (blockers.contains(blocker)) {
				candidateBlockers[blockersSize++]=blocker;
			}
		}
		
		// No blockers.
		result.addLast(new MagicCombatCreature[]{attacker});
		buildAttacker(index+1);
		result.removeLast();
		if (blockersSize == 0) {
			return;
		}
		
		// One blocker.
		if (blockersSize == 1) {
			final MagicCombatCreature blocker = candidateBlockers[0];
			blockers.remove(blocker);
			result.addLast(new MagicCombatCreature[]{attacker,blocker});
			buildAttacker(index+1);
			result.removeLast();
			blockers.add(blocker);
			return;
		}
		
        // Single blocker which does not deal lethal damage to the attacker.
        // Not sufficient: might want to chump block with multiple blockers to
        // survive the attack or damage the attackers enough to finish it off
        // with direct damage
		int lethalDamage = attacker.lethalDamage;
		for (int blockerIndex = 0; blockerIndex < blockersSize; blockerIndex++) {
			final MagicCombatCreature blocker=candidateBlockers[blockerIndex];
			if (blocker.power < lethalDamage) {
				blockers.remove(blocker);
				result.addLast(new MagicCombatCreature[]{attacker,blocker});
				buildAttacker(index+1);
				result.removeLast();
				blockers.add(blocker);
			}
		}
		
		// All combinations of blockers that deal lethal damage to the attacker.
		final MagicCombatCreature creatures[] = new MagicCombatCreature[blockersSize+1];
		creatures[0] = attacker;
		int size = 1;
		final int blockerSteps[] = new int[blockersSize];
		final int lastBlockerIndex = blockersSize-1;
		int blockerIndex = 0;
		MagicCombatCreature blocker;
		while (blockerIndex >= 0) {
			switch (blockerSteps[blockerIndex]++) {
				case 0:
					blocker = candidateBlockers[blockerIndex];
					blockers.remove(blocker);
					lethalDamage -= blocker.power;
					creatures[size++] = blocker;
					// Lethal blocking combination.
					if (lethalDamage <= 0) {
						result.addLast(Arrays.copyOf(creatures,size));
						buildAttacker(index+1);
						result.removeLast();
					} else if (blockerIndex < lastBlockerIndex) {
						blockerIndex++;
					}
					break;
				case 1:
					blocker = candidateBlockers[blockerIndex];
					blockers.add(blocker);
					lethalDamage += blocker.power;
					size--;
					if (blockerIndex < lastBlockerIndex) {
						blockerIndex++;
					}
					break;
				case 2:
					blockerSteps[blockerIndex--] = 0;
					break;
			}
		}
	}

	private void build() {
		final MagicCombatCreatureBuilder creatureBuilder=new MagicCombatCreatureBuilder(game,attackingPlayer,defendingPlayer);
		
        // Check if none of the defending player's creatures can block.
		if (!creatureBuilder.buildBlockers()) {
			return;
		}
		
		// Check if none of the attackers can be blocked.
        blockers=creatureBuilder.getBlockers();
		if (!creatureBuilder.buildBlockableAttackers()) {
			return;
		}
		
		attackers=new MagicCombatCreature[creatureBuilder.getAttackers().size()];
		creatureBuilder.getAttackers().toArray(attackers);
		final boolean defending=game.getScorePlayer()==defendingPlayer;
        
        // number of blocking options is max_blocks
        double max_blocks = 1;
        for (final MagicPermanent blocker : creatureBuilder.getCandidateBlockers()) {
	        max_blocks *= creatureBuilder.getBlockableAttackers(blocker).size();
        }

		// determine which algorithm to use.
        if (fast) {
			results=new MagicSingleScoreRanking(defending);
			if (attackers.length>MAX_ATTACKERS||game.getRelativeTurn()>MAX_TURN) {
				combatScore=new MagicFastCombatScore(defendingPlayer,game.getScorePlayer());
			} else {
				combatScore=new MagicGameCombatScore(game,attackingPlayer,defendingPlayer);
			}
		} else {
			results=new MagicMultipleScoreRanking(MAX_RESULTS,defending);
			combatScore=new MagicGameCombatScore(game,attackingPlayer,defendingPlayer);
		}
		
		// find best combinations of attackers and blockers.
		result=new MagicDeclareBlockersResult(0,0);
		position=0;
        
        if (max_blocks > 1e5) {
            System.err.println("WARNING. Number of blocking options is " + max_blocks);
        }

        if (max_blocks > 1e6) {
            buildBlockersFast();
        } else {
		    buildAttacker(0);
        }
	}

	public Collection<Object> buildResults() {
		// Caching for better speed and immediate mode for triggers.
		game.setImmediate(true);
		attackingPlayer.setCached(game,true);
		defendingPlayer.setCached(game,true);
		results = null;
		build();
		game.setImmediate(false);
		attackingPlayer.setCached(game,false);
		defendingPlayer.setCached(game,false);
		return results == null ? EMPTY_RESULT : results.getResults();
	}
}
