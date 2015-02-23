def EFFECT1 = MagicRuleEventAction.create("Return target permanent to its owner's hand.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target nonblack creature. It can't be regenerated.");

def EFFECT3 = MagicRuleEventAction.create("Destroy target artifact.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicTargetChoice.Negative("target nonblack creature"),
                    MagicTargetChoice.NEG_TARGET_ARTIFACT
                ),
                this,
                "Choose one\$ - return target permanent to its owner's hand; " +
                "or destroy target nonblack creature. It can't be regenerated; " +
                "or destroy target artifact." 
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
