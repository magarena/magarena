[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicMustAttackTargetPicker.create(),
                this,
                "Target creature\$ attacks this turn if able. " +
                "If the buyback cost was payed, return SN to its owner's hand as it resolves."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.AttacksEachTurnIfAble));
                if (event.isBuyback()) {
                    game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
                }
            });
        }
    }
]
