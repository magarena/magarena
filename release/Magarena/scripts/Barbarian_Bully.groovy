def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),4));
    } else {
        game.doAction(new ChangeTurnPTAction(event.getPermanent(),2,2));
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                MagicDiscardEvent.Random(source),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +2/+2 until end of turn unless a player has SN deal 4 damage to him or her."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer().getOpponent(),
                new MagicMayChoice("Have SN deal 4 damage to you?"),
                action,
                "PN may\$ have SN deal 4 damage to him or her. If PN doesn't, SN gets +2/+2 until end of turn."
            ));
        }
    }
]
