[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +2/+2 until end of turn. If it's an artifact creature, it gains indestructible until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,2,2));
                if (it.hasType(MagicType.Artifact) && it.hasType(MagicType.Creature)) {
                    game.doAction(new GainAbilityAction(it, MagicAbility.Indestructible));
                }
            });
        }
    }
]
