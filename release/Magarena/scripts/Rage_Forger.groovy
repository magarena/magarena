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
            final Collection<MagicPermanent> shamans = game.filterPermanents(SHAMAN_CREATURE_YOU_CONTROL.except(event.getPermanent()));
            for (final MagicPermanent creature : shamans) {
                game.doAction(new ChangeCountersAction(creature,MagicCounterType.PlusOne,1));
            }
        }
    }
]
