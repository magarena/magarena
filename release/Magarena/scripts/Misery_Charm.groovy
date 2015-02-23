def EFFECT1 = MagicRuleEventAction.create("Destroy target Cleric.");

def EFFECT2 = MagicRuleEventAction.create("Return target Cleric card from your graveyard to your hand.");

def EFFECT3 = MagicRuleEventAction.create("Target player loses 2 life.");


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.Negative("target Cleric"),
                    MagicTargetChoice.Positive("a Cleric card in your graveyard"),
                    MagicTargetChoice.NEG_TARGET_PLAYER
                ),
                this,
                "Choose one\$ - destroy target Cleric; " +
                "or return target Cleric card from your graveyard to your hand; " +
                "or target player loses 2 life.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.executeModalEvent(game, EFFECT1, EFFECT2, EFFECT3);
        }
    }
]
