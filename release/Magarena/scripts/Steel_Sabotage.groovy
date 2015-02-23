def EFFECT1 = MagicRuleEventAction.create("Counter target artifact spell.");

def EFFECT2 = MagicRuleEventAction.create("Return target artifact to its owner's hand.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target artifact spell"),
                    MagicTargetChoice.TARGET_ARTIFACT
                ),
                this,
                "Choose one\$ - counter target artifact spell; " +
                "or return target artifact to its owner's hand.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
