[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return upkeepPlayer != permanent.getController() ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN untaps SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new UntapAction(event.getPermanent()));
        }
    }
]
