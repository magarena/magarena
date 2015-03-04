[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Pay cumulative upkeep?"),
                this,
                "Have an opponent put a 1/1 red Survivor creature token onto the battlefield for each age counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Age,1));
            final int age = event.getPermanent().getCounters(MagicCounterType.Age);
            if (event.isYes()) {
                game.doAction(new MagicPlayTokensAction(
                    event.getPlayer().getOpponent(),
                    TokenCardDefinitions.get("1/1 red Survivor creature token"),
                    age
                ));
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
