def EFFECT2 = MagicRuleEventAction.create("Destroy target artifact.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    NEG_TARGET_PLAYER,
                    NEG_TARGET_ARTIFACT,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ — (1) exile all cards from target player's graveyard; " +
                "or (2) destroy target artifact; " +
                "or (3) each creature deals 1 damage to its controller.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                event.processTargetPlayer(game, {
                    for (final MagicCard card : new MagicCardList(it.getGraveyard())) {
                        game.doAction(new ShiftCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
                    }
                });
            } else if (event.isMode(3)) {
                CREATURE.filter(event) each {
                    game.doAction(new DealDamageAction(it,it.getController(),1));
                }
            } else {
                event.executeModalEvent(game, EFFECT2, EFFECT2, EFFECT2);
            }
        }
    }
]
