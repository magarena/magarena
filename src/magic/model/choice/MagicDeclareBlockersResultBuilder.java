package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.score.MagicCombatScore;
import magic.model.score.MagicFastCombatScore;
import magic.model.score.MagicGameCombatScore;
import magic.model.score.MagicMultipleScoreRanking;
import magic.model.score.MagicScoreRanking;
import magic.model.score.MagicSingleScoreRanking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MagicDeclareBlockersResultBuilder {

    private static final Collection<Object> EMPTY_RESULT =
        Collections.<Object>singletonList(new MagicDeclareBlockersResult(0,0));
    private static final int MAX_RESULTS=12;
    private static final int MAX_ATTACKERS=3;
    private static final int MAX_TURN=1;
    private static final double MIN_WARN    = 1e5;
    private static final double MIN_SWITCH  = 1e5;
    private static final double NUM_SAMPLES = 1e4;

    private final MagicGame game;
    private final MagicPlayer attackingPlayer;
    private final MagicPlayer defendingPlayer;
    private final boolean fast;
    private MagicScoreRanking results;
    private MagicDeclareBlockersResult result;
    private MagicCombatScore combatScore;
    private MagicCombatCreature[] attackers;
    private Set<MagicCombatCreature> blockers;
    private int position;

    MagicDeclareBlockersResultBuilder(final MagicGame game,final MagicPlayer defendingPlayer,final boolean fast) {
        this.game=game;
        this.defendingPlayer=defendingPlayer;
        this.attackingPlayer=defendingPlayer.getOpponent();
        this.fast=fast;

        build();
    }

    Collection<Object> getResults() {
        return results == null ? EMPTY_RESULT : results.getResults();
    }

    private void buildBlockersFast() {
        System.err.println("Running randomized blocking algorithm");

        //sample NUM_SAMPLES random blocks
        final MagicRandom rng = new MagicRandom(attackers.length + blockers.size());
        for (int i = 0; i < NUM_SAMPLES; i++) {
            final Map<Integer, List<MagicCombatCreature>> block =
                    new HashMap<>();
            for (int j = 0; j < attackers.length; j++) {
                block.put(j, new ArrayList<>(5));
                block.get(j).add(attackers[j]);
            }
            for (final MagicCombatCreature blocker : blockers) {
                //determine attackers it can block
                final List<Integer> choices = new ArrayList<>();
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

    private void buildBlockersForAttacker(final int index) {

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
        final MagicCombatCreature[] candidateBlockers=new MagicCombatCreature[attacker.candidateBlockers.length];
        int blockersSize=0;
        for (final MagicCombatCreature blocker : attacker.candidateBlockers) {
            if (blockers.contains(blocker)) {
                candidateBlockers[blockersSize++]=blocker;
            }
        }

        // No blockers.
        result.addLast(new MagicCombatCreature[]{attacker});
        buildBlockersForAttacker(index+1);
        result.removeLast();
        if (blockersSize == 0) {
            return;
        }

        // One blocker.
        if (blockersSize == 1) {
            final MagicCombatCreature blocker = candidateBlockers[0];
            blockers.remove(blocker);
            result.addLast(new MagicCombatCreature[]{attacker,blocker});
            buildBlockersForAttacker(index+1);
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
                buildBlockersForAttacker(index+1);
                result.removeLast();
                blockers.add(blocker);
            }
        }

        // All combinations of blockers that deal lethal damage to the attacker.
        final MagicCombatCreature[] creatures = new MagicCombatCreature[blockersSize+1];
        creatures[0] = attacker;
        int size = 1;
        final int[] blockerSteps = new int[blockersSize];
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
                        buildBlockersForAttacker(index+1);
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

        if (max_blocks > MIN_WARN) {
            System.err.println("WARNING. Number of blocking options is " + max_blocks);
        }

        if (max_blocks > MIN_SWITCH) {
            buildBlockersFast();
        } else {
            buildBlockersForAttacker(0);
        }
    }
}
