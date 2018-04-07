package magic.ai;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.choice.MagicCombatCreature;

import java.util.Set;

public class ArtificialScoringSystem {

    public static final int WIN_GAME_SCORE=100000000;
    public static final int LOSE_GAME_SCORE=-WIN_GAME_SCORE;
    public static final int ITEM_ON_STACK_SCORE=-1;
    public static final int UNEQUIP_SCORE=-100;
    public static final int UNNECESSARY_EQUIP_SCORE=-1000;

    private static final int[] LIFE_SCORES={
        0,1000,2000,3000,4000,
        4500,5000,5500,6000,6500,
        7000,7400,7800,8200,8600,
        9000,9200,9400,9600,9800,
        10000
    };

    private static final int[] POISON_SCORES={
        5000,4700,4400,4100,3800,
        3400,3000,2500,2000,1000,
        0
    };

    private static final int MAX_LIFE=LIFE_SCORES.length-1;

    private static final int MAX_POISON=10;

    private static final int LIFE_ABOVE_MULTIPLIER=100;

    private static final int UNKNOWN_CARD_SCORE=300;

    private static final int PERMANENT_SCORE=300;

    public static int getTurnScore(final MagicGame game) {
        return Math.max(0,10-(game.getTurn()-1)>>1);
    }

    public static int getLoseGameScore(final MagicGame game) {
        // Lose score is lowered in function of the turn and phase when it occurs. Encourages AI to win as fast as possible.
        return LOSE_GAME_SCORE+game.getTurn()*2500+game.getPhase().getType().ordinal()*200;
    }

    public static int getCardDefinitionScore(final MagicCardDefinition cardDefinition) {
        return getCardDefinitionScore(cardDefinition, 1);
    }

    // score for a card that gets put into play without paying the mana cost
    public static int getFreeCardDefinitionScore(final MagicCardDefinition cardDefinition) {
        return getCardDefinitionScore(cardDefinition, 0);
    }

    private static int getCardDefinitionScore(final MagicCardDefinition cardDefinition, final int costFactor) {
        if (cardDefinition.isLand()) {
            int score=(int)(cardDefinition.getValue()*50);
            for (final MagicColor color : MagicColor.values()) {
                score+=cardDefinition.getManaSource(color)*50;
            }
            return score;
        }
        final int score=(int)(cardDefinition.getValue()*100) - costFactor * cardDefinition.getConvertedCost() * 20;
        if (cardDefinition.isCreature()) {
            return score+(cardDefinition.getCardPower()+cardDefinition.getCardToughness())*10;
        } else if (!cardDefinition.isToken()) {
            return score+cardDefinition.getRemoval()*50+cardDefinition.getRarity()*30;
        } else {
            return score;
        }
    }

    public static int getCardScore(final MagicCard card) {
        return card.isKnown()?card.getCardDefinition().getScore():UNKNOWN_CARD_SCORE;
    }

    public static int getFreeCardScore(final MagicCard card) {
        return card.isKnown()?card.getCardDefinition().getFreeScore():UNKNOWN_CARD_SCORE;
    }

    public static int getFixedPermanentScore(final MagicPermanent permanent) {
        int score = permanent.getCardScore();
        if (permanent.isCreature()) {
            score+=permanent.getActivations().size()*50;
            score+=permanent.getManaActivations().size()*80;
        } else {
            score+=PERMANENT_SCORE;
            if (permanent.isEquipment()) {
                score+=100;
            }
        }
        return score;
    }

    public static int getVariablePermanentScore(final MagicPermanent permanent) {
        int score = permanent.getCountersScore()*30;
        if (!permanent.canTap()) {
            score+=getTappedScore(permanent);
        }
        if (permanent.isCreature()) {
            // used to consider pt and abilities without EOT effects, now includes EOT effects
            final MagicPowerToughness pt=permanent.getPowerToughness();
            final Set<MagicAbility> abilityFlags=permanent.getAbilityFlags();
            score+=pt.power()*300+pt.getPositiveToughness()*200+MagicAbility.getScore(abilityFlags)*(pt.getPositivePower()+1)/2;
            score+=permanent.getEquipmentPermanents().size()*50+permanent.getAuraPermanents().size()*100;
        }
        return score;
    }

    public static int getTappedScore(final MagicPermanent permanent) {
        return permanent.isCreature()?-10:-5;
    }

    public static int getLifeScore(final int life) {
        if (life>MAX_LIFE) {
            return LIFE_SCORES[MAX_LIFE]+(life-MAX_LIFE)*LIFE_ABOVE_MULTIPLIER;
        } else if (life>=0) {
            return LIFE_SCORES[life];
        } else {
            return 0;
        }
    }

    public static int getPoisonScore(final int poison) {
        if (poison>MAX_POISON) {
            return POISON_SCORES[MAX_POISON];
        }
        return POISON_SCORES[poison];
    }

    public static int getManaScore(final int amount) {
        return -amount;
    }

    public static int getAttackerScore(final MagicCombatCreature attacker) {
        int score=attacker.power*5+attacker.lethalDamage*2-attacker.candidateBlockers.length;
        for (final MagicCombatCreature blocker : attacker.candidateBlockers) {
            score-=blocker.power;
        }
        // Dedicated attacker.
        if (attacker.hasAbility(MagicAbility.AttacksEachTurnIfAble) ||
            attacker.hasAbility(MagicAbility.CannotBlock)) {
            score+=10;
        }
        // Abilities for attacking.
        if (attacker.hasAbility(MagicAbility.Trample) ||
            attacker.hasAbility(MagicAbility.Vigilance)) {
            score+=8;
        }
        // Dangerous to block.
        if (!attacker.normalDamage ||
            attacker.hasAbility(MagicAbility.FirstStrike) ||
            attacker.hasAbility(MagicAbility.Indestructible)) {
            score+=7;
        }
        return score;
    }

    public static int getMillScore(final int amount) {
        return -amount;
    }
}
