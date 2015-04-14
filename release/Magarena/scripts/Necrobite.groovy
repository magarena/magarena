[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicDeathtouchTargetPicker.create(),
                this,
                "Target creature\$ gains deathtouch until end of turn. " +
                "Regenerate it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainAbilityAction(
                    it,
                    MagicAbility.Deathtouch
                ));
                game.doAction(new MagicRegenerateAction(it));
            });
        }
    }
]
