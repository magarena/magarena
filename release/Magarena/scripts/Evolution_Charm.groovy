def EFFECT1 = MagicRuleEventAction.create("Search your library for a basic land card, reveal it, put it into your hand, then shuffle your library.");

def EFFECT2 = MagicRuleEventAction.create("Return target creature card from your graveyard to your hand.");

def EFFECT3 = MagicRuleEventAction.create("Target creature gains flying until end of turn.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    MagicTargetChoice.Positive("a creature card from your graveyard"),
                    MagicTargetChoice.POS_TARGET_CREATURE
                ),
                this,
                "Choose one\$ - search your library for a basic land card, reveal it, put it into your hand, then shuffle your library; " +
                "or return target creature card from your graveyard to your hand; " +
                "or target creature gains flying until end of turn.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
