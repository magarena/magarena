[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all nonblack creatures. " + 
                "SN deals X plus 3 damage to PN, where X is the number of creatures that died this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final DestroyAction destroy = new DestroyAction(NONBLACK_CREATURE.filter(event));
            game.doAction(destroy);
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),destroy.getNumDestroyed() + 3));
        }
    }
]
