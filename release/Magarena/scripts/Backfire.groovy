[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
             return (damage.getSource() == enchantedCreature && damage.getTarget() == permanent.getOwner()) ?
                new MagicEvent(
                    permanent,
                    enchantedCreature.getController(),
                    damage.getDealtAmount(),
                    this,
                    "SN deals " + damage.getDealtAmount() +
                    " damage to " + enchantedCreature.getController() + ".") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getPlayer(),
                event.getRefInt()
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]