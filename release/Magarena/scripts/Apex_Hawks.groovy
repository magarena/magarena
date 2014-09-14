[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent,final MagicPayedCost payedCost) {
            if (payedCost.isKicked()) {
                final int amount = payedCost.getKicker();
                game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount));
            }
            return MagicEvent.NONE;
        }
    }
]
