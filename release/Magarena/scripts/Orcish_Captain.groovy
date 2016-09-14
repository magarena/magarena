def TARGET_ORC_CREATURE = new MagicTargetChoice("target Orc creature");

def winAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeTurnPTAction(event.getRefPermanent(), 2, 0));
}

def loseAct = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeTurnPTAction(event.getRefPermanent(), 0, -2));
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{1}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_ORC_CREATURE,
                this,
                "Flip a coin. If PN wins the flip, target Orc creature\$ gets +2/+0 until end of turn. "+
                "If PN loses the flip, it gets -0/-2 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
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
