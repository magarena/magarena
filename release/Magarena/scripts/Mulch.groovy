[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Reveal the top four cards of your library. Put all land cards revealed this way into your hand and the rest into your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top4 = event.getPlayer().getLibrary().getCardsFromTop(4) ;
            for (final MagicCard top : top4) {
                game.doAction(new MagicRevealAction(top));
                game.doAction(new MagicRemoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MagicMoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasType(MagicType.Land) ?
                      MagicLocationType.OwnersHand :
                      MagicLocationType.Graveyard
                ));
            }
        }
    }
]
