def EFFECT1 = MagicRuleEventAction.create("Target creature gains islandwalk until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("Target creature gets +2/-1 until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("Target player discards a card.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTargetChoice.TARGET_CREATURE,
                    MagicTargetChoice.NEG_TARGET_PLAYER
                ),
                this,
                "Choose one\$ - target creature gains islandwalk until end of turn; " +
                "or target creature gets +2/-1 until end of turn; " +
                "or target player discards a card.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
