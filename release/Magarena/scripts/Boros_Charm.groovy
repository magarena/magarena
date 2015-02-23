def EFFECT1 = MagicRuleEventAction.create("SN deals 4 damage to target player.");

def EFFECT2 = MagicRuleEventAction.create("Permanents you control gain indestructible until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("Target creature gains double strike until end of turn.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target player"),
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.POS_TARGET_CREATURE
                ),
                this,
                "Choose one\$ - SN deals 4 damage to target player; " +
                "or permanents you control gain indestructible until end of turn; " +
                "or target creature gains double strike until end of turn.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
