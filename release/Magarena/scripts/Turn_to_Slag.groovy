[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(5),
                this,
                "SN deals 5 damage to target creature. Destroy all Equipment attached to that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanentList equipment = new MagicPermanentList();
                equipment.addAll(it.getEquipmentPermanents());
                game.doAction(new DealDamageAction(event.getSource(),it,5));
                game.doAction(new DestroyAction(equipment));
            });
        }
    }
]
