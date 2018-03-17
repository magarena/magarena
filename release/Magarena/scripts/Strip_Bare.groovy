[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Destroy all Auras and Equipment attached to target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanentList equipment = new MagicPermanentList();
                equipment.addAll(it.getEquipmentPermanents());
                game.doAction(new DestroyAction(it.getAuraPermanents()));
                game.doAction(new DestroyAction(equipment));
            });
        }
    }
]
