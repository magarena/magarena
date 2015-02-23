def EFFECT1 = MagicRuleEventAction.create("Target player gains 3 life.");

def EFFECT2 = MagicRuleEventAction.create("Prevent the next 3 damage that would be dealt to target creature or player this turn.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target player"),
                    MagicTargetChoice.Negative("target creature or player")
                ),
                this,
                "Choose one\$ - target player gains 3 life; " +
                "or prevent the next 3 damage that would be dealt to target creature or player this turn.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
