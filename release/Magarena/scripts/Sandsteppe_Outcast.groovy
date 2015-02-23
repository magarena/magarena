def COUNTER_EFFECT = MagicRuleEventAction.create("Put a +1/+1 counter on SN.");

def CounterTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return COUNTER_EFFECT.getEvent(permanent);
    }
}

def TOKEN_EFFECT = MagicRuleEventAction.create("Put a 1/1 white Spirit creature token with flying onto the battlefield.");

def TokenTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return TOKEN_EFFECT.getEvent(permanent);
    }
}

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
            if (event.isMode(1)) {
                game.executeTrigger(CounterTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            } else if (event.isMode(2)) {
                game.executeTrigger(TokenTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    }
]
