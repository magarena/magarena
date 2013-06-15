[
    new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            return permanent.isController(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_OPPONENT,
                    lifeChange.amount,
                    this,
                    "Target opponent\$ loses RN life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicChangeLifeAction(
                        player,
                        -event.getRefInt()
                    ));
                }
            });
        }
    }
]
