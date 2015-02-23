def choice = MagicTargetChoice.Negative("target nonartifact, nonblack creature");

def EFFECT1 = MagicRuleEventAction.create("Put a 2/2 black Zombie creature token onto the battlefield.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target nonartifact, nonblack creature. It can't be regenerated.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    choice
                ),
                this,
                "Choose one\$ - put a 2/2 black Zombie creature token onto the battlefield; " +
                "or destroy target nonartifact, nonblack creature\$. It can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
