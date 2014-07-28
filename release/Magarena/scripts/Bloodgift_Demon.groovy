[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_PLAYER,
                    this,
                    "Target player\$ draws a card and loses 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicDrawAction(it));
                game.doAction(new MagicChangeLifeAction(it,-1));
            });
        }
    }
]
