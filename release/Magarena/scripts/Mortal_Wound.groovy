[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return permanent.getEnchantedPermanent() == target ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Destroy RN."
                ): 
                MagicEvent.NONE;
        }
        @Override       
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(new DestroyAction(creature));
        }
    }
]
