[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Remove a +1/+1 counter?"),
                this,
                "PN may\$ remove a +1/+1 counter from SN. If PN doesn't, sacrifice SN and " + 
                "it deals damage equal to the number of +1/+1 counters on it to each creature without flying and each player."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = new MagicRemoveCounterEvent(event.getPermanent(), MagicCounterType.PlusOne, 1);
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
            } else {
                final int amt = event.getPermanent().getCounters(MagicCounterType.PlusOne)
                game.doAction(new SacrificeAction(event.getPermanent()));
                for (final MagicPermanent target : game.filterPermanents(CREATURE_WITHOUT_FLYING)) {
                    game.doAction(new DealDamageAction(
                        event.getSource(),
                        target,
                        amt
                    ));
                }
                for (final MagicPlayer player : game.getAPNAP()) {
                    game.doAction(new DealDamageAction(
                        event.getSource(),
                        player,
                        amt
                    ));
                }
            }
        }
    }
]
