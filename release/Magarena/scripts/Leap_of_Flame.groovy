[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicKickerChoice(
                    MagicTargetChoice.POS_TARGET_PERMANENT, 
                    MagicManaCost.create("{U}{R}"), 
                    true, 
                    true
                ),
                MagicFirstStrikeTargetPicker.create(),
                this,
                "Target creature\$ gets +1/+0 and gains flying" +
                "and first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                    game.doAction(new MagicSetAbilityAction(
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
