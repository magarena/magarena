def EFFECT1 = MagicRuleEventAction.create("Destroy target enchantment.");

def EFFECT2 = MagicRuleEventAction.create("Exile target attacking creature.");

def EFFECT3 = MagicRuleEventAction.create("Draw a card, then discard a card.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NEG_TARGET_ENCHANTMENT,
                    MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE,
                    MagicTargetChoice.NONE
                ),
                this,
                "Choose one\$ - destroy target enchantment; " +
                "or exile target attacking creature; " +
                "or draw a card, then discard a card.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
