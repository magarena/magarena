package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.stack.MagicItemOnStack;

import java.util.ArrayList;
import java.util.List;

public abstract class MagicStackFilterImpl implements MagicTargetFilter<MagicItemOnStack> {
    public List<MagicItemOnStack> filter(final MagicGame game) {
        return filter(game, game.getTurnPlayer(), MagicTargetHint.None);
    }
    
    public List<MagicItemOnStack> filter(final MagicPlayer player) {
        return filter(player.getGame(), player, MagicTargetHint.None);
    }

    public List<MagicItemOnStack> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicItemOnStack> targets=new ArrayList<MagicItemOnStack>();

        // Items on stack
        if (acceptType(MagicTargetType.Stack)) {
            for (final MagicItemOnStack targetItemOnStack : game.getStack()) {
                if (accept(game,player,targetItemOnStack) &&
                    targetHint.accept(player,targetItemOnStack)) {
                    targets.add(targetItemOnStack);
                }
            }
        }

        return targets;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Stack;
    }
}
