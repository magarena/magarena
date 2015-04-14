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
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicDiscardEvent(source, 1),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +2/+2 until end of turn unless opponent has SN deal 4 damage to him or her."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer().getOpponent(),
                new MagicMayChoice("have SN deal 4 damage to you?"),
                action,
                "PN may\$ have SN deal 4 damage to you. If you don't, SN gets +2/+2 until end of turn."
            ));
        }
    }
]
