[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
               new MagicTapEvent(source),
               new MagicPayManaCostEvent(source, "{5}")
               
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Target player\$ puts the top X cards from his or her library into his or her graveyard, "+
                "where X is the number of cards in that player's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = it.getGraveyard().size();
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new MillLibraryAction(it, amount));
            });
        }
    }
]
