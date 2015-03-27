[
    new MagicIfLifeWouldChangeTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicChangeLifeAction act) {
            if (act.getLifeChange() > 0) {
                act.setLifeChange(0);
            }
            return MagicEvent.NONE;
        }
    },
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN loses 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-1));
        }
    },
    new MagicWhenSelfTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return permanent.isEnemy(target) ?
                new MagicEvent(
                    permanent,
                    target.getController(),
                    this,
                    "PN loses 5 life."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-5));
        }
    }
]
