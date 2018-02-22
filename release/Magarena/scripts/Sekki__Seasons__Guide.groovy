[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            return super.accept(permanent, damage) &&
                damage.getTarget() == permanent;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            // The number of tokens created is the amount of damage, not prevented damage.
            final int amount = damage.getAmount();
            final int preventedAmount = damage.prevent();
            game.logAppendMessage(permanent.getController(), "Prevent ${preventedAmount} damage.");
            return new MagicEvent(
                permanent,
                amount,
                this,
                "PN creates RN 1/1 colorless Spirit creature tokens."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, -amount));
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("1/1 colorless Spirit creature token"), amount));
        }
    }
]

