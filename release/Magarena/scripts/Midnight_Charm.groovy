def EFFECT1 = MagicRuleEventAction.create("SN deals 1 damage to target creature~and you gain 1 life.");

def EFFECT2 = MagicRuleEventAction.create("Target creature gains first strike until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("Tap target creature.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    NEG_TARGET_CREATURE,
                    POS_TARGET_CREATURE,
                    NEG_TARGET_CREATURE
                ),
                this,
                "Choose one\$ — SN deals 1 damage to target creature and you gain 1 life; " +
                "or target creature gains first strike until end of turn; " +
                "or tap target creature.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
