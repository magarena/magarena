[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.doAction(new ChangeCountersAction(
                permanent,
                MagicCounterType.Age,
                1
            ));
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Pay cumulative upkeep?"),
                this,
                "PN may\$ put a -1/-1 counter on SN for each Age counter on it. " +
                "If he or she doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.MinusOne,
                    event.getPermanent().getCounters(MagicCounterType.Age)
                ));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
