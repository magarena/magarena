[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getController().controlsPermanent(MagicSubType.Ogre) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls no Ogres, sacrifice a creature."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Ogre) == false) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),SACRIFICE_CREATURE));
            }
        }
    }
]
