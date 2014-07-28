[
    new MagicPermanentActivation(
        [MagicCondition.YOUR_TURN_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Discard"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{W}{U}{B}{R}{G}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(2,2),
                this,
                "SN gets +2/+2 and gains fear until end of turn. Target creature\$ gets -2/-2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,-2,-2));
                game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),2,2));
                game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Fear));
            });
        }
    }
]
