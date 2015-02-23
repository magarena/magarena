def EFFECT1 = MagicRuleEventAction.create("Put a +1/+1 counter on SN.");

def EFFECT2 = MagicRuleEventAction.create("Search your library for a basic land card, reveal it, then shuffle your library and put that card on top of it.");

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
                "Choose one\$ - put a +1/+1 counter on SN; or search your library for a basic land card, reveal it, then shuffle your library and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2);
        }
    }
]
