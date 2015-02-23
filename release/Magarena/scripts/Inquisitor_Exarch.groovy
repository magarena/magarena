def GAIN_LIFE_EFFECT = MagicRuleEventAction.create("You gain 2 life.");

def LOSE_LIFE_EFFECT = MagicRuleEventAction.create("Target opponent loses 2 life.");

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
                "Choose one\$ - You gain 2 life; or target opponent\$ loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                GAIN_LIFE_EFFECT.getEvent(event.getSource()).executeEvent(game, event.getChosen());
            } else if (event.isMode(2)) {
                LOSE_LIFE_EFFECT.getEvent(event.getSource()).executeEvent(game, event.getChosen());
            }
        }
    }
]
