[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Remove a counter?"),
                this,
                "PN may\$ remove a +1/+1 counter from SN. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPermanent().getCounters(MagicCounterType.PlusOne) >= 1) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,-1));
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
