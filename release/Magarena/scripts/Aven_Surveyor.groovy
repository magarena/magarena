def EFFECT1 = MagicRuleEventAction.create("Put a +1/+1 counter on SN.");

def EFFECT2 = MagicRuleEventAction.create("Return target creature to its owner's hand.");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.TARGET_CREATURE
                ),
                payedCost,
                this,
                "Choose one\$ - put a +1/+1 counter on SN; or return target creature\$ to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
