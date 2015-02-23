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
                "or destroy target artifact.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
