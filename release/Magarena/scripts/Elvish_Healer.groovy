[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Prevent"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE_OR_PLAYER,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next 1 damage that would be dealt to target creature or player\$ this turn. " +
                "If that creature is green, prevent 2 damage instead."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = (it.isCreaturePermanent() && it.hasColor(MagicColor.Green)) ? 2 : 1;
                game.doAction(new PreventDamageAction(it,amount));
            });
        }
    }
]
