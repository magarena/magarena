[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = permanent.getCounters(MagicCounterType.Time);
            pt.set(amount,amount);
        }
    },
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = game.filterPermanents(permanent.getController(),MagicTargetFilterFactory.TARGET_ISLAND_YOU_CONTROL).size();
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.Time,
                amount,
                true
            ));
            return MagicEvent.NONE;
        }
    }
]
