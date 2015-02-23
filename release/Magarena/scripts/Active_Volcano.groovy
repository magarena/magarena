def EFFECT1 = MagicRuleEventAction.create("Destroy target blue permanent.");

def EFFECT2 = MagicRuleEventAction.create("Return target island to its owner's hand.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target blue permanent"),
                    MagicTargetChoice.Negative("target Island")
                ),
                this,
                "Choose one\$ - destroy target blue permanent; " +
                "or return target Island to its owner's hand.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
