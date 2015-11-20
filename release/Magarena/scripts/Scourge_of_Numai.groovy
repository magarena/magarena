[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicSubType.Ogre) == false ?
                new MagicEvent(
                    permanent,
                    this,
                    "If PN controls no Ogres, he or she loses 2 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Ogre) == false) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
            }
        }
    }
]
