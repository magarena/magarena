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
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                this,
                "Prevent the next 1 damage that would be dealt to target creature\$ and each other creature that shares a color with it this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicPermanent creature : CREATURE.filter(event)) {
                    if (creature == it || creature.shareColor(it)) {
                        game.doAction(new PreventDamageAction(creature, 1));
                    }
                }
            });
        }
    }
]
