def EFFECT1 = MagicRuleEventAction.create("Counter target red spell.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target red permanent.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target red spell"),
                    MagicTargetChoice.Negative("target red permanent")
                ),
                this,
                "Choose one\$ - counter target red spell; " +
                "or destroy target red permanent."
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
