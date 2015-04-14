[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Exile SN. Each player shuffles his or her graveyard and hand into his or her library,"+
                " then draws seven cards. You untap up to six lands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard timespiral = event.getCardOnStack().getCard();
                game.doAction(new MagicRemoveCardAction(timespiral,MagicLocationType.Stack));
                game.doAction(new MagicMoveCardAction(timespiral,MagicLocationType.Stack,MagicLocationType.Exile));
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                final MagicCardList hand = new MagicCardList(player.getHand());
                for (final MagicCard card : graveyard) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
                }
                for (final MagicCard card : hand) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.OwnersLibrary));
                }
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new DrawAction(player,7));
            }
            game.addEvent(new MagicRepeatedPermanentsEvent(
                event.getSource(),
                TARGET_LAND,
                6,
                MagicChainEventFactory.Untap
            ));
        }
    }
]
