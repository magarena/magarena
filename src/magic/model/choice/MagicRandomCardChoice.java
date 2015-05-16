package magic.model.choice;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;

import java.util.Collection;
import java.util.Collections;
import magic.model.IUIGameController;

public class MagicRandomCardChoice extends MagicChoice {

    private final int amount;

    public MagicRandomCardChoice(final int amount) {
        super("");
        this.amount=amount;
    }

    private MagicCardChoiceResult discard(final MagicPlayer player, final MagicSource source) {
        final MagicCardChoiceResult result=new MagicCardChoiceResult();
        final MagicCardList hand=new MagicCardList(player.getHand());
        hand.remove(source);
        hand.shuffle();
        final int actualAmount=Math.min(hand.size(),amount);
        for (int i = 0; i < actualAmount; i++) {
            result.add(hand.get(i));
        }
        return result;
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
        return Collections.<Object>singletonList(discard(player, source));
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
        return new Object[]{discard(player, source)};
    }

}
