package magic.model.score;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicAbility;
import magic.model.MagicPlayer;
import magic.model.choice.MagicCombatCreature;
import magic.model.choice.MagicDeclareBlockersResult;

/** Simplified combat damage model, not taking into account all abilities. */
public class MagicFastCombatScore implements MagicCombatScore {

    private final MagicPlayer defendingPlayer;
    private final MagicPlayer scorePlayer;

    public MagicFastCombatScore(final MagicPlayer defendingPlayer,final MagicPlayer scorePlayer) {

        this.defendingPlayer=defendingPlayer;
        this.scorePlayer=scorePlayer;
    }

    @Override
    public int getScore(final MagicDeclareBlockersResult result) {

        final int startLife=defendingPlayer.getLife();
        int score=-ArtificialScoringSystem.getLifeScore(startLife);
        int life=startLife;
        for (final MagicCombatCreature[] creatures : result) {

            final MagicCombatCreature attacker=creatures[0];
            if (creatures.length==1) {
                life-=attacker.power;
            } else {
                int remaining=attacker.power;
                int lethalDamage=attacker.lethalDamage;
                final boolean deathtouch=attacker.hasAbility(MagicAbility.Deathtouch);
                for (int index=1;index<creatures.length;index++) {

                    final MagicCombatCreature blocker=creatures[index];
                    lethalDamage=blocker.hasAbility(MagicAbility.Deathtouch)?0:lethalDamage-blocker.power;
                    if (blocker.hasAbility(MagicAbility.Lifelink)) {
                        life+=blocker.power;
                    }
                    if (remaining>0) {
                        final int damage=Math.min(deathtouch?1:remaining,blocker.lethalDamage);
                        if ((deathtouch||damage>=blocker.lethalDamage)&&!blocker.hasAbility(MagicAbility.Indestructible)) {
                            score-=blocker.score;
                        }
                        remaining-=damage;
                    }
                }
                if (remaining>0&&attacker.hasAbility(MagicAbility.Trample)) {
                    life-=remaining;
                }
                if (lethalDamage<=0&&!attacker.hasAbility(MagicAbility.Indestructible)) {
                    score+=attacker.score;
                }
            }
        }
        score+=(life<=0)?ArtificialScoringSystem.LOSE_GAME_SCORE:ArtificialScoringSystem.getLifeScore(life);
        return (defendingPlayer==scorePlayer)?score:-score;
    }
}
