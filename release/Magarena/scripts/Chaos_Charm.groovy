def EFFECT1 = MagicRuleEventAction.create("Destroy target Wall.");

def EFFECT2 = MagicRuleEventAction.create("SN deals 1 damage to target creature.");

def EFFECT3 = MagicRuleEventAction.create("Target creature gains haste until end of turn.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target Wall"),
                    MagicTargetChoice.Negative("target creature"),
                    MagicTargetChoice.Positive("target creature")
                ),
                this,
                "Choose one\$ - destroy target Wall; " +
                "or SN deals 1 damage to target creature; " +
                "or target creature gains haste until end of turn.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
