def TEXT1 = "You gain 5 life."

def TEXT2 = "Prevent the next 5 damage that would be dealt to target creature this turn."

def EFFECT1 = MagicRuleEventAction.create(TEXT1);

def EFFECT2 = MagicRuleEventAction.create(TEXT2);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.isKicked() ? 
                    MagicTargetChoice.POS_TARGET_CREATURE :
                    new MagicOrChoice(
                        MagicChoice.NONE,
                        MagicTargetChoice.POS_TARGET_CREATURE
                    ),
                this,
                payedCost.isKicked() ?
                    "PN gains 5 life. " + TEXT2 + "\$":
                    "Choose one\$ — • " + TEXT1 + " • " + TEXT2 + "\$"
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
