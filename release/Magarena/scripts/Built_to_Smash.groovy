def choice = MagicTargetChoice.Positive("target attacking creature")

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                MagicPumpTargetPicker.create(),
                this,
                "Target attacking creature\$ gets +3/+3 until end of turn. If it's an artifact creature, it gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,3,3));
                if (it.hasType(MagicType.Artifact) && it.hasType(MagicType.Creature)) {
                    game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
                }
            });
        }
    }
]
