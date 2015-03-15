def TEXT1 = "Creatures you control get +1/+1 until end of turn."

def TEXT2 = "Untap all creatures you control."

def EFFECT1 = MagicRuleEventAction.create(TEXT1);

def EFFECT2 = MagicRuleEventAction.create(TEXT2);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.isKicked() ? 
                    MagicChoice.NONE :
                    new MagicOrChoice(
                        MagicChoice.NONE,
                        MagicChoice.NONE
                    ),
                this,
                payedCost.isKicked() ?
                    TEXT1 + " " + TEXT2 :
                    "Choose one\$ — • " + TEXT1 +  " • " + TEXT2
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isKicked()) {
                event.executeAllEvents(game, EFFECT1, EFFECT2);
            } else {
                event.executeModalEvent(game, EFFECT1, EFFECT2);
            }
        }
    }
]
