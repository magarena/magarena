def EFFECT2 = MagicRuleEventAction.create("SN deals 2 damage to each creature.");

def EFFECT3 = MagicRuleEventAction.create("Put two +1/+1 counters on target creature.");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    NEG_TARGET_PLAYER,
                    MagicChoice.NONE,
                    POS_TARGET_CREATURE
                ),
                this,
                "Choose one\$ - (1) exile all cards from target player's graveyard; " +
                "or (2) SN deals 2 damage to each creature; " +
                "or (3) put two +1/+1 counters on target creature.\$" 
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                event.processTargetPlayer(game, {
                    final MagicCardList graveyard = new MagicCardList(it.getGraveyard());
                    for (final MagicCard cardGraveyard : graveyard) {
                        game.doAction(new RemoveCardAction(cardGraveyard,MagicLocationType.Graveyard));
                        game.doAction(new MoveCardAction(cardGraveyard,MagicLocationType.Graveyard,MagicLocationType.Exile));
                    }
                });
            } else {
                event.executeModalEvent(game, EFFECT2, EFFECT2, EFFECT3);
            }
        }
    }
]
