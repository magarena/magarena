def EFFECT1 = MagicRuleEventAction.create("Destroy target artifact creature.");

def EFFECT2 = MagicRuleEventAction.create("Attacking creatures get +1/+0 until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("Target creature with power 2 or less can't be blocked this turn.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target artifact creature"),
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.Positive("target creature with power 2 or less")
                ),
                this,
                "Choose one\$ - destroy target artifact creature; " +
                "or attacking creatures get +1/+0 until end of turn; " +
                "or target creature with power 2 or less can't be blocked this turn." 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.addEvent(EFFECT1.getEvent(event.getSource()));
            } else if (event.isMode(2)) {
                game.addEvent(EFFECT2.getEvent(event.getSource()));
            } else if (event.isMode(3)) {
                game.addEvent(EFFECT3.getEvent(event.getSource()));
            }
        }
    }
]
