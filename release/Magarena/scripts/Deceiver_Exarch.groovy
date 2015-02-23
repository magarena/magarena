def TAP_EFFECT = MagicRuleEventAction.create("Tap target permanent an opponent controls.");

def UNTAP_EFFECT = MagicRuleEventAction.create("Untap target permanent you control.");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicTargetChoice.TARGET_PERMANENT_YOU_CONTROL,
                    MagicTargetChoice.TARGET_PERMANENT_AN_OPPONENT_CONTROLS
                ),
                payedCost,
                this,
                "Choose one\$ - Untap target permanent you control; or tap target permanent an opponent controls.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                UNTAP_EFFECT.getEvent(event.getSource()).executeEvent(game, event.getChosen());
            } else if (event.isMode(2)) {
                TAP_EFFECT.getEvent(event.getSource()).executeEvent(game, event.getChosen());
            }
        }
    }
]
