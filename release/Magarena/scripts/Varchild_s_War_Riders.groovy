[
    new AtYourUpkeepTrigger() {
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
                "PN may\$ have an opponent create a 1/1 red Survivor creature token for each age counter on SN. " +
                "If he or she doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokensAction(
                    event.getPlayer().getOpponent(),
                    CardDefinitions.getToken("1/1 red Survivor creature token"),
                    event.getPermanent().getCounters(MagicCounterType.Age)
                ));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
