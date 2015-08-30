[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return (permanent == damage.getSource() &&
                    damage.getTarget().isCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "SN deals 3 damage to RN's controller and 3 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new DealDamageAction(permanent, event.getRefPermanent().getController(), 3));
            game.doAction(new DealDamageAction(permanent, event.getPlayer(), 3));
        }
    }
]
