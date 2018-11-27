def trigger = {
    final MagicPlayer player ->
    return new IfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().hasColor(MagicColor.Red) &&
                    (damage.getTarget().isPermanent() || damage.getTarget().isPlayer())
            ) {
                damage.setAmount(damage.getAmount() + 2);
            }
            return MagicEvent.NONE;
        }
    }
}

[
    new SagaChapterTrigger(3) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
            return new MagicEvent(
                permanent,
                this,
                "If a red source PN controls would deal damage to a permanent or player this turn, it deals that much damage plus to to that permanent or player instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(trigger(event.getPlayer())));
        }
    }
]

