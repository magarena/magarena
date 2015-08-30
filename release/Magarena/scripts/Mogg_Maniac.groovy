[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to target opponent\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(
                    event.getSource(),
                    it,
                    event.getRefInt()
                ));
            });
        }
    }
]
