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
                    ""+permanent.getEnchantedPermanent().getController()+" loses RN life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt(); 
            game.doAction(new MagicChangeLifeAction(event.getPermanent().getEnchantedPermanent().getController(),-amount));
        }
    }
]
