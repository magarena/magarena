package magic.test;

import java.util.concurrent.atomic.AtomicInteger;

import magic.ai.MagicAIImpl;
import magic.data.CardDefinitions;
import magic.model.*;
import magic.model.action.PlayCardFromStackAction;
import magic.model.player.AiProfile;
import magic.model.player.HumanProfile;
import magic.model.stack.MagicCardOnStack;

public abstract class TestGameBuilder {

    private static final AtomicInteger currentId=new AtomicInteger(1);

    public static void addToLibrary(final MagicPlayer player, final String name, final int count) {
        final MagicCardDefinition cardDefinition=CardDefinitions.getCard(name);
        for (int c=count;c>0;c--) {
            player.getLibrary().addToTop(new MagicCard(cardDefinition,player,currentId.getAndIncrement()));
        }
    }

    public static void addToLibrary(final MagicPlayer player, final String name) {
        addToLibrary(player, name, 1);
    }

    public static void addToGraveyard(final MagicPlayer player, final String name, final int count) {
        final MagicCardDefinition cardDefinition=CardDefinitions.getCard(name);
        for (int c=count;c>0;c--) {
            player.getGraveyard().addToTop(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
        }
    }

    public static void addToGraveyard(final MagicPlayer player, final String name) {
        addToGraveyard(player, name, 1);
    }

    public static void addToExile(final MagicPlayer player,final String name,final int count) {
        final MagicCardDefinition cardDefinition=CardDefinitions.getCard(name);
        for (int c=count;c>0;c--) {
            player.getExile().addToTop(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
        }
    }

    public static void addToHand(final MagicPlayer player, final String name, final int count) {
        final MagicCardDefinition cardDefinition=CardDefinitions.getCard(name);
        for (int c=count;c>0;c--) {
            player.addCardToHand(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
        }
    }

    public static void addToHand(final MagicPlayer player, final String name) {
        addToHand(player, name, 1);
    }

    public static MagicPermanent createPermanent(final MagicPlayer player, final String name, final boolean tapped, final int count) {
        final MagicGame game = player.getGame();
        final MagicCardDefinition cardDefinition=CardDefinitions.getCard(name);
        MagicPermanent lastPermanent= MagicPermanent.NONE;
        for (int c=count;c>0;c--) {

            final MagicCard card=new MagicCard(cardDefinition,player,currentId.getAndIncrement());
            final MagicCardOnStack cardOnStack = new MagicCardOnStack(card, player, MagicPayedCost.NOT_SPELL);
            final MagicPermanent permanent=new MagicPermanent(currentId.getAndIncrement(),card,player);
            lastPermanent=permanent;

            game.doAction(new PlayCardFromStackAction(cardOnStack) {
                @Override
                protected MagicPermanent createPermanent(final MagicGame game) {
                    return permanent;
                }
            });

            game.getEvents().clear();
            game.checkStatePutTriggers();
            game.getStack().removeAllItems();
            permanent.clearState(MagicPermanentState.Summoned);
            if (tapped) {
                permanent.setState(MagicPermanentState.Tapped);
            }
        }
        return lastPermanent;
    }

    public static MagicPermanent createPermanent(final MagicPlayer player,final String name,final int amount){
        return createPermanent(player,name,false,amount);
    }

    public static MagicPermanent createPermanent(final MagicPlayer player,final String name){
        return createPermanent(player,name,false,1);
    }

    protected abstract MagicGame getGame();

    public static MagicGame buildGame(final String id) {
        MagicGame game = null;
        try { //load class by name
            final TestGameBuilder gb = Class.forName("magic.test." + id).asSubclass(TestGameBuilder.class).newInstance();
            game = gb.getGame();
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.err.println("ERROR! Unable to build game " + id);
            throw new RuntimeException(ex);
        }
        return game;
    }

    public static MagicDuel createDuel(final MagicAIImpl aAiType, final int aAiLevel) {
        final MagicDuel duel=new MagicDuel();

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final DuelPlayerConfig player1=new DuelPlayerConfig(HumanProfile.create("Player"),profile);
        final DuelPlayerConfig player2=new DuelPlayerConfig(AiProfile.create(aAiType, aAiLevel),profile);

        duel.setPlayers(new DuelPlayerConfig[]{player1,player2});
        duel.setStartPlayer(0);
        return duel;
    }

    public static MagicDuel createDuel() {
        return createDuel(MagicAIImpl.MMABFast, 6);
    }
}
