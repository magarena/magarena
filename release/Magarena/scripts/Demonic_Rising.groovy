[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer eotPlayer) {
            return (permanent.isController(eotPlayer) &&
                    eotPlayer.getNrOfPermanents(MagicType.Creature) == 1) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 5/5 black Demon creature " +
                    "token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(MagicType.Creature) == 1) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("5/5 black Demon creature token with flying")
                ));
            }
        }
    }
]
