[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicFirstStrikeTargetPicker.create(),
                this,
                "Target creature\$ gets +1/+0 and gains flying " +
                "and first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                    game.doAction(new MagicGainAbilityAction(
                        creature,
                        [
                            MagicAbility.Flying,
                            MagicAbility.FirstStrike
                        ]
                    ));
                }
            });
        }
    }
]
