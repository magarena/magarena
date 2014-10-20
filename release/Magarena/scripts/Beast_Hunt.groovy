[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Reveal the top three cards of your library. Put all creature cards revealed this way into your hand and the rest into your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top3 = event.getPlayer().getLibrary().getCardsFromTop(3) ;
            for (final MagicCard top : top3) {
                game.doAction(new MagicRevealAction(top));
                game.doAction(new MagicRemoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MagicMoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasType(MagicType.Creature) ?
                      MagicLocationType.OwnersHand :
                      MagicLocationType.Graveyard
                ));
            }
        }
    }
]
