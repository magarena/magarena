[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{X}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                amount,
                this,
                "Target player\$ puts the top RN cards of his or her library " +
                "into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicMillLibraryAction(
                    it,
                    event.getRefInt()
                ));
            });
        }
    }
]
