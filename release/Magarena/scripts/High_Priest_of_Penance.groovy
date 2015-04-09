[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        NEG_TARGET_NONLAND_PERMANENT
                    ),
                    MagicDestroyTargetPicker.Destroy,
                    this,
                    "Destroy target nonland permanent\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                event.processTargetPermanent(game, {
                    game.doAction(new MagicDestroyAction(it));
                });
            }
        }
    }
]
