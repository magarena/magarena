def EFFECT1 = MagicRuleEventAction.create("Creatures you control get +1/+1 until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("Untap all creatures you control.");

def CARDTEXT = "Choose one\$ — • Creatures you control get +1/+1 until end of turn." +
               " • Untap all creatures you control. " +
               "Choose both if you paid the entwine cost.";

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return payedCost.isKicked() ?
                new MagicEvent(
                    cardOnStack,
                    this,
                    CARDTEXT
                ):
                new MagicEvent(
                    cardOnStack,
                    new MagicOrChoice(
                        MagicChoice.NONE,
                        MagicChoice.NONE
                    ),
                    this,
                    CARDTEXT
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isKicked()) {
                game.addEvent((EFFECT1).getEvent(event.getSource()));
                game.addEvent((EFFECT2).getEvent(event.getSource()));
            } else if (event.isMode(1)) {
                game.addEvent((EFFECT1).getEvent(event.getSource()));
            } else if (event.isMode(2)) {
                game.addEvent((EFFECT2).getEvent(event.getSource()));
            }
        }
    }
]
