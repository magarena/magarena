def PLUS_TWO_EFFECT = MagicRuleEventAction.create("Target creature gets +2/+0 until end of turn.");

def MINUS_TWO_EFFECT = MagicRuleEventAction.create("Target creature gets -0/-2 until end of turn.");

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
                "Choose one\$ - Target creature gets +2/+0 until end of turn; or target creature gets -0/-2 until end of turn.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                PLUS_TWO_EFFECT.getEvent(event.getSource()).executeEvent(game, event.getChosen());
            } else if (event.isMode(2)) {
                MINUS_TWO_EFFECT.getEvent(event.getSource()).executeEvent(game, event.getChosen());
            }
        }
    }
]
