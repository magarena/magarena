[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_EQUIPPED_CREATURE,
                MagicDefaultTargetPicker.create(), //Leave options open
                this,
                "Unattach all Equipment from target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                EQUIPMENT_ATTACHED_TO_SOURCE.filter(it) each {
                    game.doAction(new AttachAction(it, MagicPermanent.NONE));
                }
            });
        }
    }
]
