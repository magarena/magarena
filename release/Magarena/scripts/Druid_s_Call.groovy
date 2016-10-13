[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return damage.getTarget() == enchanted ?
                new MagicEvent(
                    permanent,
                    enchanted.getController(),
                    amount,
                    this,
                    "PN creates RN 1/1 green Squirrel creature tokens."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 green Squirrel creature token"),
                event.getRefInt()
            ));
        }
    }
]
