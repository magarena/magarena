[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicMustAttackTargetPicker.create(),
                this,
                "Target creature\$ attacks this turn if able. Draw a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainAbilityAction(it,MagicAbility.AttacksEachTurnIfAble));
                game.doAction(new DrawAction(event.getPlayer()));
            });
        }
    }
]
