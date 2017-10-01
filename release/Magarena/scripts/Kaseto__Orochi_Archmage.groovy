[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "{G}{U}"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{G}{U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target creature\$ can't be blocked this turn. If that creature is a Snake, it gets +2/+2 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainAbilityAction(it, MagicAbility.Unblockable));
                if (it.hasSubType(MagicSubType.Snake)) {
					game.doAction(new ChangeTurnPTAction(it,2,2));
                }
            });
        }
    }
]
