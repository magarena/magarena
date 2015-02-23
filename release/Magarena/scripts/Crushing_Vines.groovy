def EFFECT1 = MagicRuleEventAction.create("Destroy target creature with flying.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target artifact.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target creature with flying"),
                    MagicTargetChoice.Negative("target artifact")
                ),
                this,
                "Choose one\$ - destroy target creature with flying; " +
                "or destroy target artifact."
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
