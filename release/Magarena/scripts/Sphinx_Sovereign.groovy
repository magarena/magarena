[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "If SN is untapped, PN gains 3 life. " +
                    "Otherwise, each opponent loses 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().isUntapped()){
                game.doAction(new ChangeLifeAction(event.getPlayer(), 3));
            } else {
                game.doAction(new ChangeLifeAction(event.getPlayer().getOpponent(), -3));
            }
        }
    }
]
