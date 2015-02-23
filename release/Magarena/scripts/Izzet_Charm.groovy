def EFFECT1 = MagicRuleEventAction.create("Counter target noncreature spell unless its controller pays {2}.");

def EFFECT2 = MagicRuleEventAction.create("SN deals 2 damage to target creature.");

def EFFECT3 = MagicRuleEventAction.create("Draw two cards, then discard two cards.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target noncreature spell"),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicTargetChoice.NONE
                ),
                this,
                "Choose one\$ - counter target noncreature spell unless its controller pays {2}; " +
                "or SN deals 2 damage to target creature; " +
                "or draw two cards, then discard two cards." 
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
