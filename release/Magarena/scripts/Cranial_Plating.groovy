def equip = new MagicEquipActivation(MagicRegularCostEvent.build("{B}{B}"));

[
    new MagicPermanentActivation(
        [MagicCondition.NOT_CREATURE_CONDITION],
        new MagicActivationHints(MagicTiming.Equipment),
        "Attach"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return equip.getCostEvent(source);
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return equip.getPermanentEvent(source, payedCost);
        }
    }
]
