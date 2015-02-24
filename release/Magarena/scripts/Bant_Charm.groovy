def EFFECT1 = MagicRuleEventAction.create("Destroy target artifact.");

def EFFECT3 = MagicRuleEventAction.create("Counter target instant spell.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NEG_TARGET_ARTIFACT,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicTargetChoice.Negative("target instant spell")
                ),
                this,
                "Choose one\$ - destroy target artifact; " +
                "or put target creature on the bottom of its owner's library; " +
                "or counter target instant spell.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.addEvent(EFFECT1.getEvent(event.getSource()));
            } else if (event.isMode(2)) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.BottomOfOwnersLibrary));
            });
            } else if (event.isMode(3)) {
                game.addEvent(EFFECT3.getEvent(event.getSource()));
            }
        }
    }
]
