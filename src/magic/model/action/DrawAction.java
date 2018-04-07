package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.ArrayList;
import java.util.List;

public class DrawAction extends MagicAction {

    private final MagicPlayer player;
    private final int amount;
    private List<MagicCard> drawnCards;

    public DrawAction(final MagicPlayer player) {
        this(player, 1);
    }

    public DrawAction(final MagicPlayer player,final int amount) {
        this.player=player;
        this.amount=amount;
    }

    @Override
    public void doAction(final MagicGame game) {
        drawnCards= new ArrayList<>();
        final MagicCardList library=player.getLibrary();
        int score=0;
        for (int count=amount;count>0;count--) {
            if (library.isEmpty()) {
                if (MagicGame.LOSE_DRAW_EMPTY_LIBRARY) {
                    game.doAction(new LoseGameAction(player,LoseGameAction.DRAW_REASON));
                }
                break;
            }
            final MagicCard card=library.removeCardAtTop();
            player.addCardToHand(card);
            player.incDrawnCards();
            drawnCards.add(card);
            score+=ArtificialScoringSystem.getCardScore(card);
            for (final MagicTrigger<MagicCard> trigger : card.getCardDefinition().getDrawnTriggers()) {
                game.executeTrigger(trigger,MagicPermanent.NONE,card,card);
            }
            game.executeTrigger(MagicTriggerType.WhenOtherDrawn,card);
        }
        setScore(player,score);
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        for (int index=drawnCards.size()-1;index>=0;index--) {
            final MagicCard card=drawnCards.get(index);
            player.removeCardFromHand(card);
            player.getLibrary().addToTop(card);
            player.decDrawnCards();
        }
    }
}
