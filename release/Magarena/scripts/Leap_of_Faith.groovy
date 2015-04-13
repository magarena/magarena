[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicFlyingTargetPicker.create(),
                this,
                "Target creature\$ gains flying until end of turn. " +
                "Prevent all damage that would be dealt to that creature this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Flying));
                game.doAction(new AddTurnTriggerAction(
                    it,
                    MagicIfDamageWouldBeDealtTrigger.PreventDamageDealtTo
                ));
            });
        }
    }
]
