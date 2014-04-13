[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return ScryXEvent(permanent, permanent.getController(),3);
        }
        public MagicEvent ScryXEvent(final MagicSource source,final MagicPlayer player, final int X) {
            final MagicCardList processedCards = new MagicCardList();
            final MagicCardList bottomList = new MagicCardList();
            final MagicCardList topList = new MagicCardList();
            final int size = player.getLibrary().size();
            final int actualX = (size > X) ? X : size;
            for(int i = 0; i < actualX; i++) {
                bottomList.add(player.getLibrary().get(size-i-1));
            }
            return new MagicEvent(
                source,
                player,
                {
                    final MagicGame game, final MagicEvent event ->
                    game.addEvent(new MagicEvent(
                        source,
                        player,
                        new MagicFromCardListChoice(bottomList, actualX, true),
                        {
                            final MagicGame bottomGame, final MagicEvent bottomEvent ->
                            bottomEvent.processChosenCards(bottomGame, {
                                final MagicCard card ->
                                processedCards.add(card);
                                bottomGame.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                                bottomGame.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.BottomOfOwnersLibrary));
                            });
                            for(int i = 0; i < actualX - processedCards.size(); i++) {
                                topList.add(player.getLibrary().get(size-i-1));
                            }
                            game.addEvent(new MagicEvent(
                                source,
                                player,
                                new MagicFromCardListChoice(topList, topList.size()),
                                {
                                    final MagicGame topGame, final MagicEvent topEvent ->
                                    topEvent.processChosenCards(topGame, {
                                    final MagicCard card ->
                                        topGame.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                                        topGame.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
                                    });
                                },
                                "Choose cards to be put at top of PN's library in any order."
                            ));
                        },
                        "Choose cards to be put at bottom of PN's library."
                    ));
                },
                "Scry "+X
            );
        }   
    }
]
