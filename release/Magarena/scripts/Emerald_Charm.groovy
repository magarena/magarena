def EFFECT1 = MagicRuleEventAction.create("Untap target permanent.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target non-Aura enchantment.");

def EFFECT3 = MagicRuleEventAction.create("Target creature loses flying until end of turn.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Positive("target permanent"),
                    MagicTargetChoice.Negative("target non-aura enchantment"),
                    MagicTargetChoice.NEG_TARGET_CREATURE
                ),
                this,
                "Choose one\$ - untap target permanent; " +
                "or destroy target non-Aura enchantment; " +
                "or target creature loses flying until end of turn.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
