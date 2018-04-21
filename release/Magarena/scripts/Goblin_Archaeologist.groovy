def winAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new DestroyAction(event.getRefPermanent()));
    game.doAction(new UntapAction(event.getPermanent()));
}

def loseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new SacrificeAction(event.getPermanent()));
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{R}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_ARTIFACT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Flip a coin. If PN wins the flip, destroy target artifact\$ and untap SN. "+
                "If PN loses the flip, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.addEvent(new MagicCoinFlipEvent(
                    event,
                    it,
                    winAct,
                    loseAct
                ));
            });
        }
    }
]
