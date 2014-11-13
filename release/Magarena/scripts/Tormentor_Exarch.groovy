def PLUS_TWO_EFFECT = MagicRuleEventAction.create("Target creature gets +2/+0 until end of turn.");

def PlusTwoTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return PLUS_TWO_EFFECT.getEvent(permanent);
    }
}

def MINUS_TWO_EFFECT = MagicRuleEventAction.create("Target creature gets -0/-2 until end of turn.");

def MinusTwoTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return MINUS_TWO_EFFECT.getEvent(permanent);
    }
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicTargetChoice.NEG_TARGET_CREATURE
                ),
                payedCost,
                this,
                "Choose one\$ - Target creature gets +2/+0 until end of turn; or target creature gets -0/-2 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.executeTrigger(PlusTwoTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            } else if (event.isMode(2)) {
                game.executeTrigger(MinusTwoTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    }
]
