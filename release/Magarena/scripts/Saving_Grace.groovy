def redirectTrigger = {
    final MagicPlayer player ->
    return new IfDamageWouldBeDealtTrigger(MagicTrigger.REDIRECT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (player.isFriend(damage.getTarget())) {
                damage.setTarget(permanent);
            }
            return MagicEvent.NONE;
        }
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getEnchantedPermanent(),
                this,
                "All damage that would be dealt this turn to PN and permanents PN controls is dealt to RN instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(event.getRefPermanent(), redirectTrigger(event.getPlayer())));
        }
    }
]

