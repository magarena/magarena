def winAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new DealDamageAction(event.getPermanent(), event.getRefTarget(), 2));
}

def loseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new DealDamageAction(event.getPermanent(), event.getPermanent(), 2));
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(2),
                this,
                "Flip a coin. If PN wins the flip, SN deals 2 damage to target creature or player. "+
                "If PN loses the flip, SN deals 2 damage to itself."
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
