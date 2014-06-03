[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent.getEnchantedPermanent() &&
                    permanent.isOpponent(damage.getTarget()) &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD
                    ),
                    this,
                    "PN may\$ return target creature card\$ from his or her graveyard to his or her hand."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    final MagicCard card ->
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
