[
    new MagicWhenPutIntoGraveyardTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            final MagicCard card = act.card;
            if (card.isEnemy(game.getActiveSource())) {
                act.setToLocation(MagicLocationType.Play);
                game.doAction(new MagicPlayCardAction(card));
            }
            return MagicEvent.NONE;
        }
    }
]
