[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN looks at the top five cards of his or her library. Then PN puts one of them into his or her hand and the rest on top of his or her library in any order."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList choiceList = new MagicCardList();
            final int size = event.getPlayer().getLibrary().size();
            final int amount = (size > 4) ? 5 : size;
            if (size == 1) game.doAction(new MagicDrawAction(event.getPlayer(),1));
            else if (size > 1) {
                for(int i = 1; i <= amount; i++) {
                    choiceList.add(event.getPlayer().getLibrary().get(size-i));
                }
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList,1),
                    {
                        final MagicGame game2, final MagicEvent event2 ->
                        event2.processChosenCards(game2, {
                            final MagicCard card ->
                            game2.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                            game2.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
                        }); 
                        game.addEvent(new MagicEvent(
                            event2.getSource(),
                            {
                                final MagicGame game3, final MagicEvent event3 ->
                                final MagicCardList putBackList = new MagicCardList();
                                for(int i = 2; i <= amount; i++) {
                                    putBackList.add(event3.getPlayer().getLibrary().get(size-i));
                                }
                                game3.addEvent(new MagicEvent(
                                    event3.getSource(),
                                    new MagicFromCardListChoice(putBackList,putBackList.size()),
                                    {
                                        final MagicGame game4, final MagicEvent event4 ->
                                        event4.processChosenCards(game4, {
                                            final MagicCard card ->
                                            game4.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                                            game4.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.TopOfOwnersLibrary));
                                        });
                                    },
                                    ""
                                ));           
                            },
                            ""
                        ));
                    },
                    ""
                )); 
            }
        }
    }
]
