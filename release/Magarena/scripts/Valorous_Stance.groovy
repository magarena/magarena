def EFFECT1 = MagicRuleEventAction.create("Target creature gains indestructible until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target creature with toughness 4 or greater.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Positive("target creature"),
                    MagicTargetChoice.Negative("target creature with toughness 4 or greater")
                ),
                this,
                "Choose one\$ - target creature gains indestructible until end of turn; " +
                "or destroy target creature with toughness 4 or greater.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
