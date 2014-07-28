[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.FirstMain,true),
        "Detain"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{3}{W}{U}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS,
                new MagicNoCombatTargetPicker(true,true,false),
                this,
                "Detain target nonland permanent\$ an opponent controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDetainAction(event.getPlayer(), it));
            });
        }
    }
]
