def EFFECT1 = MagicRuleEventAction.create("Return target permanent to its owner's hand.");

def EFFECT2 = MagicRuleEventAction.create("Target creature gets -4/-4 until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("Creatures you control get +2/+0 until end of turn.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicTargetChoice.NONE
                ),
                this,
                "Choose one\$ - return target permanent to its owner's hand; " +
                "or target creature gets -4/-4 until end of turn; " +
                "or creatures you control get +2/+0 until end of turn." 
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
