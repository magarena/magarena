def equip = new MagicEquipActivation(MagicRegularCostEvent.build("{B}{B}"));

[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amt = permanent.getController().getNrOfPermanents(MagicType.Artifact);
            pt.add(amt, 0);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    },
    new MagicPermanentActivation(
        [MagicCondition.NOT_CREATURE_CONDITION],
        new MagicActivationHints(MagicTiming.Equipment,2),
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
