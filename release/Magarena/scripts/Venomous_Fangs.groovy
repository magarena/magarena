[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.getSource() == permanent.getEnchantedPermanent() &&
                    damage.getTarget().isCreaturePermanent() ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPermanent(),
                    this,
                    "SN destroys RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getRefPermanent()));
        }
    }
]
