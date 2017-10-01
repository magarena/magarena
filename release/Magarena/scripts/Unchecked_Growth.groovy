[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +4/+4 until end of turn. If it's a Spirit, it gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,4,4));
                if (it.hasSubType(MagicSubType.Spirit)) {
                    game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
                }
            });
        }
    }
]

