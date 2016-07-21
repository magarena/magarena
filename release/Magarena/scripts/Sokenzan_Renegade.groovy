[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getOpponent().getHandSize() > permanent.getController().getHandSize() ?
                new MagicEvent(
                    permanent,
                    this,
                    "The player with the most cards in hand gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getOpponent().getHandSize() > event.getPlayer().getHandSize()) {
                game.doAction(new GainControlAction(event.getPlayer().getOpponent(), event.getPermanent()));
            }
        }
    }
]
