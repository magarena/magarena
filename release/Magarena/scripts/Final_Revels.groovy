def EFFECT1 = MagicRuleEventAction.create("All creatures get +2/+0 until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("All creatures get -0/-2 until end of turn.");

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
                "Choose one\$ - all creatures get +2/+0 until end of turn; " +
                "or all creatures get -0/-2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
