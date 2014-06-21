[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_EQUIPPED_CREATURE,
                MagicDefaultTargetPicker.create(), //Leave options open
                this,
                "Unattach all Equipment from target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final Collection<MagicPermanent> equipmentList = game.filterPermanents(creature.getController(),MagicTargetFilterFactory.EQUIPMENT_YOU_CONTROL);
                for (final MagicPermanent equipment : equipmentList) {
                    if (equipment.getEquippedCreature() == creature) {
                        game.doAction(new MagicAttachAction(equipment,MagicPermanent.NONE));
                    }
                }
            });
        }
    }
]
