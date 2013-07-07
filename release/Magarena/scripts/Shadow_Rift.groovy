[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicShadowTargetPicker.getInstance(),
                this,
                "Target creature\$ gains shadow until end of turn. Draw a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Shadow));
                    game.doAction(new MagicDrawAction(event.getPlayer()));
                }
            });
        }
    }
]
