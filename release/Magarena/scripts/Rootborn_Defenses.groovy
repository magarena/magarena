[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Populate. Creatures you control are indestructible this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPopulateEvent(event.getSource()));
            game.doAction(new MagicAddStaticAction(new MagicStatic(
                MagicLayer.Ability,
                MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                MagicStatic.UntilEOT) {
                @Override
                public void modAbilityFlags(
                        final MagicPermanent source,
                        final MagicPermanent permanent,
                        final Set<MagicAbility> flags) {
                    flags.add(MagicAbility.Indestructible);
                }
            }));
        }
    }
]
