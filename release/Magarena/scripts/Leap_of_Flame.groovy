[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicFirstStrikeTargetPicker.create(),
                this,
                "Target creature\$ gets +1/+0 and gains flying " +
                "and first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,1,0));
                game.doAction(new MagicGainAbilityAction(
                    it,
                    [
                        MagicAbility.Flying,
                        MagicAbility.FirstStrike
                    ]
                ));
            });
        }
    }
]
