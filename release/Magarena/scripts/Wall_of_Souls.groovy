[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_OPPONENT,
                    new MagicDamageTargetPicker(amount),
                    amount,
                    this,
                    "SN deals RN damage to target opponent\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,event.getRefInt()));
            });
        }
    }
]
