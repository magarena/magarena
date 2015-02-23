def EFFECT1 = MagicRuleEventAction.create("SN deals 3 damage to each creature.");

def EFFECT2 = MagicRuleEventAction.create("SN deals 3 damage to each player.");

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
                "Choose one\$ - SN deals 3 damage to each creature; " +
                "or SN deals 3 damage to each player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
