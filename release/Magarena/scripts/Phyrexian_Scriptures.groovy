[
    new SagaChapterTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent , final MagicCounterChangeTriggerData data) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Choose a target creature?", POS_TARGET_CREATURE),
                this,
                "PN puts a +1/+1 counter on up to one target creature\$\$. " +
                "That creature becomes an artifact in addition to its other types."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new ChangeCountersAction(
                        event.getPlayer(),
                        it,
                        MagicCounterType.PlusOne,
                        1
                    ));
                    game.doAction(new AddStaticAction(
                        it,
                        MagicStatic.Artifact
                    ));
                });
            }
        }
    }
]
