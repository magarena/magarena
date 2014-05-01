[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.ALL_INSTANCE,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                this,
                "Choose a color\$. " +
                "Target creature\$ PN controls gains protection from the chosen color until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicColor color = event.getChosenColor();
            final MagicAbility protection = color.getProtectionAbility();
            event.processTarget(game, {
                final MagicTarget target ->
                game.doAction(new MagicGainAbilityAction(target, protection));
                game.addEvent(new MagicScryEvent(event));
            });  
        }
    }
]
