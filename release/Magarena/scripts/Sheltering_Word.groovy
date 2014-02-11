[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE,
                MagicShroudTargetPicker.create(),
                this,
                "Target creature\$ gains hexproof until end of turn. PN gains life equal to RN's toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Hexproof));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),creature.getToughness()));
            });
        }
    }
]
