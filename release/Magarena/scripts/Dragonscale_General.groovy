[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN bolsters X, where X is the number of tapped creatures PN controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(TAPPED_CREATURE_YOU_CONTROL);
            game.logAppendValue(player,amount);
            game.addEvent(new MagicBolsterEvent(event, amount));
        }
    }
]
