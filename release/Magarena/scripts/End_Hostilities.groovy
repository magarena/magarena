[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all creatures and all permanents attached to creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList perms = new MagicPermanentList();
            final Collection<MagicPermanent> targets = game.filterPermanents(CREATURE);
            for (final MagicPermanent creature : targets) {
                perms.add(creature);
                perms.addAll(creature.getEquipmentPermanents());
                perms.addAll(creature.getAuraPermanents());
            }
            game.doAction(new DestroyAction(perms));
        }
    }
]
