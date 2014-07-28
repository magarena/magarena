[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                this,
                "Prevent all damage that would be dealt to target creature\$ this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicAddTurnTriggerAction(
                    it,
                    MagicIfDamageWouldBeDealtTrigger.PreventDamageDealtTo
                ));
            });
        }
    }
]
