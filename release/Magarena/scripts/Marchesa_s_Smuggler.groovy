[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.FirstMain),
        "Haste"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{1}{U}{R}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target creature\$ gains haste and is unblockable this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Unblockable));
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Haste));
            });
        }
    }
]
