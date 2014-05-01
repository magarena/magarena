[
    new MagicWhenSelfTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return permanent.isEnemy(target) ?
                new MagicEvent(
                    permanent,
                    target.getController(),
                    this,
                    "PN loses 4 life."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-4));
        }
    }
]
