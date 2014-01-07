[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.getController() != permanent.getController() ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    this,
                    "PN puts the top two cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCardList library = event.getPlayer().getLibrary();
        final int size = library.size();
            for (int c=2;c>0;c--) {
                final MagicCard milledCard = library.getCardAtTop();
                game.doAction(new MagicRemoveCardAction(
                        milledCard,
                        MagicLocationType.OwnersLibrary));
                game.doAction(new MagicMoveCardAction(
                        milledCard,
                        MagicLocationType.OwnersLibrary,
                        MagicLocationType.Graveyard));
            }
        }
    }
]
