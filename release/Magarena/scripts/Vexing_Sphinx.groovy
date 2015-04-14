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
                "PN may\$ discard a card for each Age counter on SN. " +
                "If he or she doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicDiscardEvent(
                    event.getPermanent(),
                    event.getPlayer(),
                    event.getPermanent().getCounters(MagicCounterType.Age)
                ));
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    },
    new MagicWhenDiesTrigger() {       
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {       
            return new MagicEvent(
                permanent,
                this,
                "PN draws a card for each age counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(),event.getPermanent().getCounters(MagicCounterType.Age)));
        }
    }
]
