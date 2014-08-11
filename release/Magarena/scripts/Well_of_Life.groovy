[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer endofturnPlayer) {
            return permanent.getController().controlsPermanent(MagicTargetFilterFactory.UNTAPPED_LAND) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 2 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicTargetFilterFactory.UNTAPPED_LAND) == false) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),2));
            }
        }
    }
]
