[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Draw"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card for each Ally he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount =  player.getNrOfPermanents(MagicSubType.Ally);
            game.doAction(new DrawAction(player,amount));
        }
    }
]
