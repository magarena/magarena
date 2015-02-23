def EFFECT1 = MagicRuleEventAction.create("SN deals 1 damage to each creature without flying.");

def EFFECT2 = MagicRuleEventAction.create("SN deals 1 damage to each creature with flying.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ - SN deals 1 damage to each creature without flying; " +
                "or SN deals 1 damage to each creature with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
