[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures PN controls get +1/+1 until end of turn. " +
                "If PN has 5 or less life, those creatures gain indestructible until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets = game.filterPermanents(
                player,
                MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL
            );
            for (final MagicPermanent creature : targets) {
                game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                if (MagicCondition.FATEFUL_HOUR.accept(event.getSource())) {
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Indestructible));
                }
            }
        }
    }
]
