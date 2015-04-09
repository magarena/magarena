[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                MagicShroudTargetPicker.create(),
                this,
                "Target creature you control\$ gains hexproof until end of turn. PN gains life equal to that creature's toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Hexproof));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),it.getToughness()));
            });
        }
    }
]
