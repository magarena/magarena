[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature\$ and each other creature that shares a color with it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                for (final MagicPermanent creature : CREATURE.filter(event)) {
                    if (creature == it || creature.shareColor(it)) {
                        game.doAction(new DealDamageAction(event.getPermanent(), creature, 1));
                    }
                }
            });
        }
    }
]
