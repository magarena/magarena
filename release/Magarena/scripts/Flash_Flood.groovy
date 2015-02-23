def EFFECT1 = MagicRuleEventAction.create("Destroy target red permanent.");

def EFFECT2 = MagicRuleEventAction.create("Return target mountain to its owner's hand.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target red permanent"),
                    MagicTargetChoice.Negative("target Mountain")
                ),
                this,
                "Choose one\$ - destroy target red permanent; " +
                "or return target Mountain to its owner's hand.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
