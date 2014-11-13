def GAIN_LIFE_EFFECT = MagicRuleEventAction.create("You gain 2 life.");

def GainLifeTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return GAIN_LIFE_EFFECT.getEvent(permanent);
    }
}

def LOSE_LIFE_EFFECT = MagicRuleEventAction.create("Target opponent loses 2 life.");

def LoseLifeTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return LOSE_LIFE_EFFECT.getEvent(permanent);
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
                    MagicTargetChoice.TARGET_OPPONENT
                ),
                payedCost,
                this,
                "Choose one\$ - You gain 2 life; or target opponent loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.executeTrigger(GainLifeTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            } else if (event.isMode(2)) {
                game.executeTrigger(LoseLifeTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    }
]
