[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(2,2);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.NOT_YOUR_TURN_CONDITION.accept(source);
        }
    }, 
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = source.getOpponent().getNrOfPermanents(MagicSubType.Swamp) + 2;
            pt.set(amount,amount);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.YOUR_TURN_CONDITION.accept(source);
        }
    } 
]
