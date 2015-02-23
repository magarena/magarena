def EFFECT1 = MagicRuleEventAction.create("Target creature gains first strike until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("Target player gains 2 life.");

def EFFECT3 = MagicRuleEventAction.create("Destroy target Aura.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Positive("target creature"),
                    MagicTargetChoice.Positive("target player"),
                    MagicTargetChoice.Negative("target aura")
                ),
                this,
                "Choose one\$ - target creature gains first strike until end of turn; " +
                "or target player gains 2 life; " +
                "or destroy target Aura." 
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
