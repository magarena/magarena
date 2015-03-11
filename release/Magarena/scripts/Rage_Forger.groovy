[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on each other Shaman creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> shamans = game.filterPermanents(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.SHAMAN_CREATURE_YOU_CONTROL,
                    event.getPermanent()
                )
            );
            for (final MagicPermanent creature : shamans) {
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1));
            }
        }
    }
]
