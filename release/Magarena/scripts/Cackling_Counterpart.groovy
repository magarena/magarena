[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                MagicCopyPermanentPicker.create(),
                this,
                "PN puts a token onto the battlefield that's a copy of target creature\$ he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    it
                ));
            });
        }
    }
]
