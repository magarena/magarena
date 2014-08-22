[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer turnPlayer) {
            return permanent.isUntapped() && permanent.isController(turnPlayer) ?
                new MagicEvent(
                    permanent,
                    turnPlayer,
                    this,
                    "PN loses 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-3));
        }
    }
]
