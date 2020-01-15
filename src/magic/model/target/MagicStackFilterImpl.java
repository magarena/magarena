package magic.model.target;

import java.util.ArrayList;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;

public abstract class MagicStackFilterImpl implements MagicTargetFilter<MagicItemOnStack> {
    @Override
    public List<MagicItemOnStack> filter(final MagicSource source) {
        return filter(source, source.getController(), MagicTargetHint.None);
    }

    @Override
    public List<MagicItemOnStack> filter(final MagicPlayer player) {
        return filter(MagicSource.NONE, player, MagicTargetHint.None);
    }

    @Override
    public List<MagicItemOnStack> filter(final MagicEvent event) {
        return filter(event.getSource(), event.getPlayer(), MagicTargetHint.None);
    }

    @Override
    public List<MagicItemOnStack> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint) {
        final MagicGame game = player.getGame();
        final List<MagicItemOnStack> targets= new ArrayList<>();

        // Items on stack
        if (acceptType(MagicTargetType.Stack)) {
            for (final MagicItemOnStack targetItemOnStack : game.getStack()) {
                if (accept(source,player,targetItemOnStack) &&
                    targetHint.accept(player,targetItemOnStack)) {
                    targets.add(targetItemOnStack);
                }
            }
        }

        return targets;
    }
    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Stack;
    }

    /**
     * @return filter with added condition matching spell with specified exact converted mana cost
     */
    public MagicStackFilterImpl cmcEQ(int cmc) {
        final MagicStackFilterImpl curr = this;
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
                return curr.accept(source, player, item) && item.getConvertedCost() == cmc;
            }
        };
    }

    /**
     * @return filter with added condition matching spell with specified minimal converted mana cost
     */
    public MagicStackFilterImpl cmcGEQ(int cmc) {
        final MagicStackFilterImpl curr = this;
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
                return curr.accept(source, player, item) && item.getConvertedCost() >= cmc;
            }
        };
    }

    /**
     * @return filter with added condition matching spell with specified maximal converted mana cost
     */
    public MagicStackFilterImpl cmcLEQ(int cmc) {
        final MagicStackFilterImpl curr = this;
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
                return curr.accept(source, player, item) && item.getConvertedCost() <= cmc;
            }
        };
    }

    /**
     * @return with added condition filter matching spell controlled by you
     */
    public MagicStackFilterImpl youControl() {
        final MagicStackFilterImpl curr = this;
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
                return curr.accept(source, player, item) && item.isFriend(player);
            }
        };
    }

    /**
     * @return with added condition filter matching spell not controlled by you
     */
    public MagicStackFilterImpl youNotControl() {
        final MagicStackFilterImpl curr = this;
        return new MagicStackFilterImpl() {
            @Override
            public boolean accept(final MagicSource source, final MagicPlayer player, final MagicItemOnStack item) {
                return curr.accept(source, player, item) && item.isEnemy(player);
            }
        };
    }
}
