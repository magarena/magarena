[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$. " +
                "Creatures PN controls gain protection from the chosen color until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicAbility protection = event.getChosenColor().getProtectionAbility();
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new GainAbilityAction(it, protection));
            }
        }
    }
]
