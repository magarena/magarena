[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = permanent.getCounters(MagicCounterType.Time);
            pt.set(amount,amount);
        }
    },
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = game.filterPermanents(permanent.getController(),ISLAND_YOU_CONTROL).size();
            game.doAction(new ChangeCountersAction(permanent,MagicCounterType.Time,amount));
            return MagicEvent.NONE;
        }
    }
]
