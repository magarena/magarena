def EFFECT1 = MagicRuleEventAction.create("Target creature gets +2/+2 and gains trample until end of turn.");

def EFFECT2 = MagicRuleEventAction.create("Exile target creature with power 5 or greater.");

def EFFECT3 = MagicRuleEventAction.create("Put a 2/2 white Knight creature token with vigilance onto the battlefield.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTargetChoice.Negative("target creature with power 5 or greater"),
                    MagicTargetChoice.NONE
                ),
                this,
                "Choose one\$ - target creature gets +2/+2 and gains trample until end of turn; " +
                "or exile target creature with power 5 or greater; " +
                "or put a 2/2 white Knight creature token with vigilance onto the battlefield.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
