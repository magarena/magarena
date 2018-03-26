package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicType;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class MagicPermanentFilterImpl implements MagicTargetFilter<MagicPermanent> {
    @Override
    public List<MagicPermanent> filter(final MagicSource source) {
        return filter(source, source.getController(), MagicTargetHint.None);
    }

    @Override
    public List<MagicPermanent> filter(final MagicPlayer player) {
        return filter(MagicSource.NONE, player, MagicTargetHint.None);
    }

    @Override
    public List<MagicPermanent> filter(final MagicEvent event) {
        return filter(event.getSource(), event.getPlayer(), MagicTargetHint.None);
    }

    @Override
    public List<MagicPermanent> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint) {
        final MagicGame game = player.getGame();
        final List<MagicPermanent> targets=new ArrayList<MagicPermanent>();
        if (acceptType(MagicTargetType.Permanent)) {
            for (final MagicPlayer controller : game.getPlayers()) {
                for (final MagicPermanent targetPermanent : controller.getPermanents()) {
                    if (accept(source,player,targetPermanent) &&
                        targetHint.accept(player,targetPermanent)) {
                        targets.add(targetPermanent);
                    }
                }
            }
        }
        return targets;
    }

    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Permanent;
    }

    public MagicPermanentFilterImpl except(final MagicPermanent invalid) {
        return new MagicOtherPermanentTargetFilter(this, invalid);
    }

    /**
     * @return filter that adds "is attacking" condition
     */
    public MagicPermanentFilterImpl andAttacking() {
        final MagicPermanentFilterImpl curr = this;
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
                return curr.accept(source, player, target) && target.isAttacking();
            }
        };
    }

    /**
     * @return filter that adds condition for type of permanent
     */
    public MagicPermanentFilterImpl andType(final MagicType type) {
        final MagicPermanentFilterImpl curr = this;
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
                return curr.accept(source, player, target) && target.hasType(type);
            }
        };
    }

    /**
     * @return filter that adds "nonartifact" / "is not artifact" condition
     */
    public MagicPermanentFilterImpl andNotArtifact() {
        final MagicPermanentFilterImpl curr = this;
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
                return curr.accept(source, player, target) && !target.isArtifact();
            }
        };
    }


    /**
     * @return filter with added condition matching permanent with specified exact converted mana cost
     */
    public MagicPermanentFilterImpl cmcEQ(int cmc) {
        final MagicPermanentFilterImpl curr = this;
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
                return curr.accept(source, player, target) && target.getConvertedCost() == cmc;
            }
        };
    }

    /**
     * @return filter with added condition matching permanent with specified minimal converted mana cost
     */
    public MagicPermanentFilterImpl cmcGEQ(int cmc) {
        final MagicPermanentFilterImpl curr = this;
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
                return curr.accept(source, player, target) && target.getConvertedCost() >= cmc;
            }
        };
    }

    /**
     * @return filter with added condition matching permanent with specified maximal converted mana cost
     */
    public MagicPermanentFilterImpl cmcLEQ(int cmc) {
        final MagicPermanentFilterImpl curr = this;
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
                return curr.accept(source, player, target) && target.getConvertedCost() <= cmc;
            }
        };
    }

    /**
     * @return filter with added condition for a non-token permanent
     */
    public MagicPermanentFilterImpl nonToken() {
        final MagicPermanentFilterImpl curr = this;
        return new MagicPermanentFilterImpl() {
            @Override
            public boolean accept(MagicSource source, MagicPlayer player, MagicPermanent target) {
                return curr.accept(source, player, target) && !target.isToken();
            }
        };
    }

}
