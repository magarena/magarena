[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONBLACK_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target nonblack creature\$. It can't be regenerated. "+
                "PN gains control of all Equipment that was attached to it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanentList attached = new MagicPermanentList();
                attached.addAll(it.getEquipmentPermanents());
                game.doAction(new DestroyAction(it));
                for (final MagicPermanent equip : attached) {
                    game.doAction(new GainControlAction(event.getPlayer(), equip));
                }
            });
        }
    }
]
