def COUNTER_EFFECT = MagicRuleEventAction.create("Put a +1/+1 counter on SN.");

def TOKEN_EFFECT = MagicRuleEventAction.create("Put a 1/1 white Spirit creature token with flying onto the battlefield.");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.NONE
                ),
                payedCost,
                this,
                "Choose one\$ - Put a +1/+1 counter on SN; or put a 1/1 white Spirit creature token with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, COUNTER_EFFECT, TOKEN_EFFECT);
        }
    }
]
