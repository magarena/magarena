[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return damage.getTarget() == enchanted ?
                new MagicEvent(
                    permanent,
                    enchanted.getController(),
                    amount,
                    this,
                    "PN puts RN 1/1 green Squirrel creature tokens onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("1/1 green Squirrel creature token"),
                event.getRefInt()
            ));
        }
    }
]
