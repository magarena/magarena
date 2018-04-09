def loseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new GainControlAction(event.getPlayer().getOpponent(), event.getPermanent()));
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature or player\$. "+
                "Flip a coin. If PN loses the flip, an opponent gains control of SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getPermanent(), it, 1));
                game.addEvent(new MagicCoinFlipEvent(
                    event,
                    MagicEventAction.NONE,
                    loseAct
                ));
            });
        }
    }
]
