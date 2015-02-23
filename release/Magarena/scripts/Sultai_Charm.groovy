def EFFECT1 = MagicRuleEventAction.create("Destroy target monocolored creature.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target artifact or enchantment.");

def EFFECT3 = MagicRuleEventAction.create("Draw two cards, then discard a card.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target monocolored creature"),
                    MagicTargetChoice.Negative("target artifact or enchantment"),
                    MagicTargetChoice.NONE
                ),
                this,
                "Choose one\$ - destroy target monocolored creature; " +
                "or destroy target artifact or enchantment; " +
                "or draw two cards, then discard a card.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
