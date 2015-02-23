def EFFECT1 = MagicRuleEventAction.create("You gain 6 life.");

def EFFECT2 = MagicRuleEventAction.create("Prevent the next 6 damage that would be dealt to target creature this turn.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicTargetChoice.Positive("target creature")
                ),
                this,
                "Choose one\$ - you gain 6 life; " +
                "or prevent the next 6 damage that would be dealt to target creature this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.addEvent(EFFECT1.getEvent(event.getSource()));

            } else if (event.isMode(2)) {
                game.addEvent(EFFECT2.getEvent(event.getSource()));
            }
        }
    }
]
