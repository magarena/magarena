package magic.model.target;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicAmount;
import magic.model.MagicAmountFactory;
import magic.model.event.MagicEvent;
import magic.exception.GameException;

/** Creature permanent or player. Can be your own creatures. */
public class MagicDamageTargetPicker extends MagicTargetPicker<MagicTarget> {

    private int amount;
    private final boolean noRegeration;
    private final MagicAmount count;

    public MagicDamageTargetPicker(final int aAmount, final boolean aNoRegeneration, final MagicAmount aCount) {
        amount = aAmount;
        noRegeration = aNoRegeneration;
        count = aCount;
    }
    public MagicDamageTargetPicker(final int amount, final boolean noRegeneration) {
        this(amount, noRegeneration, MagicAmountFactory.One);
    }

    public MagicDamageTargetPicker(final int amount) {
        this(amount, false);
    }

    // use by card script effect
    public MagicDamageTargetPicker(final MagicAmount count) {
        this(-1, false, count);
    }

    @Override
    protected void setEvent(final MagicEvent event) {
        if (amount < 0 || !count.isConstant()) {
            amount = count.getAmount(event);
        }
    }

    @Override
    protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        // Player
        if (target.isPlayer()) {
            final MagicPlayer targetPlayer=(MagicPlayer)target;
            final int actualAmount=amount-targetPlayer.getPreventDamage();
            if (actualAmount<=0) {
                return 0;
            }
            final int life=targetPlayer.getLife();
            final int score;
            if (life>actualAmount) {
                score=ArtificialScoringSystem.getLifeScore(life)-ArtificialScoringSystem.getLifeScore(life-actualAmount);
            } else {
                score=ArtificialScoringSystem.WIN_GAME_SCORE;
            }
            return targetPlayer==player?-score:score;
        } else if (target.isPermanent()) {
            // Permanent
            final MagicPermanent permanent=(MagicPermanent)target;
            if (permanent.hasAbility(MagicAbility.Indestructible)) {
                return 0;
            }
            if (permanent.isRegenerated()&&!noRegeration) {
                return 0;
            }
            final int actualAmount=amount-permanent.getPreventDamage();
            if (actualAmount<=0) {
                return 0;
            }
            final int leftToughness=permanent.getToughness()-permanent.getDamage()-actualAmount;
            final int score=leftToughness<=0?permanent.getScore():20-leftToughness;
            return permanent.getController()==player?-score:score;
        } else {
            throw new GameException("target is neither MagicPlayer nor MagicPermanent", game);
        }
    }
}
