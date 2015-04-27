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
                "SN deals 1 damage to target creature\$ "+
                "and each other creature with the same name as that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicNameTargetFilter(
                    CREATURE,
                    it.getName()
                );
                final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),targetFilter);
                for (final MagicPermanent creature : targets) {
                    game.doAction(new DealDamageAction(event.getSource(),creature,1));
                }
            });
        }
    }
]
