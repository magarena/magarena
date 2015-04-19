package magic.test;

import magic.data.CardDefinitions;
import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;

import magic.model.MagicPayedCost;
import magic.model.MagicDuel;
import magic.model.MagicDeckProfile;
import magic.model.player.AiProfile;
import magic.model.player.HumanProfile;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.PlayTokenAction;
import magic.model.action.PlayCardFromStackAction;
import magic.ai.MagicAIImpl;

import java.util.concurrent.atomic.AtomicInteger;
import magic.model.MagicPlayerDefinition;

public abstract class TestGameBuilder {

    private static final AtomicInteger currentId=new AtomicInteger(1);

    public static void addToLibrary(final MagicPlayer player, final String name, final int count) {
        final MagicCardDefinition cardDefinition=CardDefinitions.getCard(name);
        for (int c=count;c>0;c--) {
            player.getLibrary().addToTop(new MagicCard(cardDefinition,player,currentId.getAndIncrement()));
        }
    }

    public static void addToGraveyard(final MagicPlayer player, final String name, final int count) {
        final MagicCardDefinition cardDefinition=CardDefinitions.getCard(name);
        for (int c=count;c>0;c--) {
            player.getGraveyard().addToTop(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
        }
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

    static void createAllTokens(final MagicGame game, final MagicPlayer player) {
        for (final MagicCardDefinition cardDefinition : TokenCardDefinitions.getAll()) {
            game.doAction(new PlayTokenAction(player,cardDefinition));
        }
    }

    public static MagicPermanent createPermanent(final MagicGame game, final MagicPlayer player, final String name, final boolean tapped, final int count) {

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
    
    public static MagicPermanent createPermanent(final MagicGame game,final MagicPlayer player,final String name,final int amount){
        return createPermanent(game,player,name,false,amount);
    }
    
    public static MagicPermanent createPermanent(final MagicGame game,final MagicPlayer player,final String name){
        return createPermanent(game,player,name,false,1);
    }

    protected abstract MagicGame getGame();

    public static MagicGame buildGame(final String id) {
        MagicGame game = null;
        try { //load class by name
            final TestGameBuilder gb = Class.forName("magic.test." + id).asSubclass(TestGameBuilder.class).newInstance();
            game = gb.getGame();
        } catch (final ClassNotFoundException ex) {
            System.err.println("ERROR! Unable to build game " + id);
            throw new RuntimeException(ex);
        } catch (final InstantiationException ex) {
            System.err.println("ERROR! Unable to build game " + id);
            throw new RuntimeException(ex);
        } catch (final IllegalAccessException ex) {
            System.err.println("ERROR! Unable to build game " + id);
            throw new RuntimeException(ex);
        }
        return game;
    }

    public static MagicDuel createDuel(final MagicAIImpl aAiType, final int aAiLevel) {
        final MagicDuel duel=new MagicDuel();

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition(HumanProfile.create("Player"),profile);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition(AiProfile.create(aAiType, aAiLevel),profile);

        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);
        return duel;
    }

    public static MagicDuel createDuel() {
        return createDuel(MagicAIImpl.MMABFast, 6);
    }
}
