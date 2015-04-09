[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{X}{B}{B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                source,
                TARGET_CREATURE,
                new MagicWeakenTargetPicker(0,amount),
                amount,
                this,
                "Target creature\$ gets -0/-RN until end of turn and " +
                "SN gets +RN/+0 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefInt();
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,0,-amount));
                game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),amount,0));
            });
        }
    }
]
