[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            for (final MagicPermanent creature : CREATURE_YOU_CONTROL.except(permanent).filter(source.getController())) {
                amount += creature.getCounters(MagicCounterType.PlusOne);
            };
            pt.add(amount,amount);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
