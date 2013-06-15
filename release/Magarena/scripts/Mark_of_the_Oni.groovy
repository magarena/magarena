[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer eotPlayer) {
            return (permanent.isController(eotPlayer) &&
                    eotPlayer.controlsPermanent(MagicSubType.Demon) == false) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    }
]
