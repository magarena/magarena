[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Until end of turn, target creature\$ gains trample and gets +X/+X, where X is the number of attacking creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainAbilityAction(it, MagicAbility.Trample));
                final int X = game.getNrOfPermanents(ATTACKING_CREATURE);
                game.doAction(new ChangeTurnPTAction(it, X, X));
            });
        }
    }
]
