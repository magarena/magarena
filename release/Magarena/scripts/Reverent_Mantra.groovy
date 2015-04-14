[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$. " +
                "All creatures gain protection from the chosen color until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicColor color = event.getChosenColor();
            final MagicAbility protection = color.getProtectionAbility();
            final Collection<MagicPermanent> targets = game.filterPermanents(CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new GainAbilityAction(target, protection));
            }
        }
    }
]
