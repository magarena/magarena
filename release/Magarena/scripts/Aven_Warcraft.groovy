[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Creatures PN controls get +0/+2 until end of turn. " + 
                "PN chooses a color\$. " + 
                "If seven or more cards are in PN's graveyard, creatures he or she controls also gain protection from the chosen color until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicAbility protection = event.getChosenColor().getProtectionAbility();
            final Boolean threshold = MagicCondition.THRESHOLD_CONDITION.accept(event.getSource());
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it, 0, 2));
                if (threshold) {
                    game.doAction(new GainAbilityAction(it, protection));
                }
            }
        }
    }
]
