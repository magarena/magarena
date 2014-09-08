[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Reveal the top three cards of your library. Put all Island cards revealed this way into your hand and the rest on the bottom of your library in any order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList library = event.getPlayer().getLibrary();
            for (int i = 0; i < 3 && library.isEmpty() == false; i++) {
                final MagicCard top = library.getCardAtTop();
                game.doAction(new MagicRemoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MagicMoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasSubType(MagicSubType.Island) ?
                      MagicLocationType.OwnersHand :
                      MagicLocationType.BottomOfOwnersLibrary
                ));
            }
        }
    }
]
