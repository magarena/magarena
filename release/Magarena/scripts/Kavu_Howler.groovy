[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Reveal the top four cards of your library. Put all Kavu cards revealed this way into your hand and the rest on the bottom of your library in any order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList library = event.getPlayer().getLibrary();
            for (int i = 0; i < 4 && library.isEmpty() == false; i++) {
                final MagicCard top = library.getCardAtTop();
                game.doAction(new MagicRemoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MagicMoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasSubType(MagicSubType.Kavu) ?
                      MagicLocationType.OwnersHand :
                      MagicLocationType.BottomOfOwnersLibrary
                ));
            }
        }
    }
]
