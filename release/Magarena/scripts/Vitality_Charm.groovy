def EFFECT1 = MagicRuleEventAction.create("Put a 1/1 green Insect creature token onto the battlefield.");

def EFFECT2 = MagicRuleEventAction.create("Target creature gets +1/+1 and gains trample until end of turn.");

def EFFECT3 = MagicRuleEventAction.create("Regenerate target Beast.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTargetChoice.Positive("target Beast")
                ),
                this,
                "Choose one\$ - put a 1/1 green Insect creature token onto the battlefield; " +
                "or target creature gets +1/+1 and gains trample until end of turn; " +
                "or regenerate target Beast.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
