[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent.getEnchantedPermanent()) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "SN deals RN damage to "+permanent.getEnchantedPermanent().getController()
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDealDamageAction(
                event.getSource(),
                event.getPermanent().getEnchantedPermanent().getController(),
                event.getRefInt()
            ));
        }
    }
]
