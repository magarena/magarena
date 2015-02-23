def EFFECT1 = MagicRuleEventAction.create("Put a +1/+1 counter on SN.");

def EFFECT2 = MagicRuleEventAction.create("Destroy target artifact.");

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
                "Choose one\$ - Put a +1/+1 counter on SN; or destroy target artifact.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
