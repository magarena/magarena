[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Remove a +1/+1 counter?"),
                this,
                "PN may\$ remove a +1/+1 counter from SN. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = new MagicRemoveCounterEvent(event.getPermanent(), MagicCounterType.PlusOne, 1);
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
