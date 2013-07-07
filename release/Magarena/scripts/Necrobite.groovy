[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicDeathtouchTargetPicker.getInstance(),
                this,
                "Target creature\$ gains deathtouch until end of turn. " +
                "Regenerate it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainAbilityAction(
                        creature,
                        MagicAbility.Deathtouch
                    ));
                    game.doAction(new MagicRegenerateAction(creature));
                }
            });
        }
    }
]
