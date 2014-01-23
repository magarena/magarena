[
    new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return permanent.isFriend(card) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                       MagicTargetChoice.TARGET_OPPONENT
                    ),
                    this,
                    "PN may\$ have target opponent\$ put the top card of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game, {
                    final MagicPlayer player ->
                    final MagicCardList library = player.getLibrary();
                    final MagicCard milledCard = library.getCardAtTop();
                    game.doAction(new MagicRemoveCardAction(
                            milledCard,
                            MagicLocationType.OwnersLibrary));
                    game.doAction(new MagicMoveCardAction(
                            milledCard,
                            MagicLocationType.OwnersLibrary,
                            MagicLocationType.Graveyard));
                });
            }
        }
    }
]
