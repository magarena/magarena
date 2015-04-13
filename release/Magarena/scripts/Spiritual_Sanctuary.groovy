[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicSubType.Plains) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN gains 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicSubType.Plains)) {
                game.doAction(new ChangeLifeAction(event.getPlayer(), 1));
            }
        }
    }
]
