def EFFECT1 = MagicRuleEventAction.create("Target creature gets +3/+3 until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("Permanents you control gain hexproof until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("Return target creature to its owner's hand.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.TARGET_CREATURE
                ),
                this,
                "Choose one\$ - target creature gets +3/+3 until end of turn; " +
                "or permanents you control gain hexproof until end of turn; " +
                "or return target creature to its owner's hand.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
