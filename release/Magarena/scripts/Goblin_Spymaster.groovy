[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isOpponent(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    eotPlayer,
                    this,
                    "PN creates a 1/1 red Goblin creature token with \"Creatures you control attack each combat if able.\""
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(event.getPlayer(),CardDefinitions.getToken("1/1 red Goblin creature token with Creatures you control attack each combat if able"),1));
        }
    }
]
