[
    new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent.getEquippedCreature() == attacker ?
                new MagicEvent(
                    permanent,
                    TARGET_PERMANENT,
                    MagicDestroyTargetPicker.Destroy,
                    this,
                    "Destroy target permanent\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
            });
        }
    }
]
