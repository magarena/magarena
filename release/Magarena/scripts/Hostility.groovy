[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            return super.accept(permanent, damage) &&
                damage.getSource().isSpell() &&
                permanent.isFriend(damage.getSource()) &&
                permanent.isOpponent(damage.getTarget());
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = damage.prevent();
            return new MagicEvent(
                permanent,
                amount,
                this,
                "Prevent RN damage. PN creates RN 3/1 red Elemental Shaman creature tokens with haste."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("3/1 red Elemental Shaman creature token with haste"), event.getRefInt()));
        }
    }
]

