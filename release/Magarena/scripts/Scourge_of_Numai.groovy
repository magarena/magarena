[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) && 
permanent.getController().controlsPermanent(MagicSubType.Ogre) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls no Ogres, lose 2 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Ogre) == false) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
            }
        }
    }
]
