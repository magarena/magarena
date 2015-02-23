def EFFECT1 = MagicRuleEventAction.create("Target creature gains haste until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("Target creature gets +2/+0 until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("SN deals 3 damage to target Wizard creature.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTargetChoice.Negative("target Wizard creature")
                ),
                this,
                "Choose one\$ - target creature gains haste until end of turn; " +
                "or target creature gets +2/+0 until end of turn; " +
                "or SN deals 3 damage to target Wizard creature.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
