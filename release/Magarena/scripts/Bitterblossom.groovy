[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN loses 1 life and puts a 1/1 black Faerie Rogue creature token with flying " +
                    "onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player,-1));
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Faerie Rogue")));
        }
    }
]
