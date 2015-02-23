def COUNTER_EFFECT = MagicRuleEventAction.create("Put a +1/+1 counter on SN.");

def CounterTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return COUNTER_EFFECT.getEvent(permanent);
    }
}

def DESTROY_EFFECT = MagicRuleEventAction.create("Destroy target artifact.");

def DestroyTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return DESTROY_EFFECT.getEvent(permanent);
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
                    MagicTargetChoice.NEG_TARGET_ARTIFACT
                ),
                payedCost,
                this,
                "Choose one\$ - Put a +1/+1 counter on SN; or destroy target artifact."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.executeTrigger(CounterTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            } else if (event.isMode(2)) {
                game.executeTrigger(DestroyTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    }
]
