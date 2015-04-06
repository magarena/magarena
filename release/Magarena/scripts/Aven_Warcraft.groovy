[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Creatures you control get +0/+2 until end of turn. " + 
                "Choose a color\$. " + 
                "If seven or more cards are in your graveyard, creatures you control also gain protection from the chosen color until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicAbility protection = event.getChosenColor().getProtectionAbility();
            final Boolean threshold = MagicCondition.THRESHOLD_CONDITION.accept(event.getSource());
            event.getPlayer().filterPermanents(MagicTargetFilterFactory.CREATURE_YOU_CONTROL) each {
                game.doAction(new MagicChangeTurnPTAction(it, 0, 2));
                if (threshold) {
                    game.doAction(new MagicGainAbilityAction(it, protection));
                }
            }
        }
    }
]
