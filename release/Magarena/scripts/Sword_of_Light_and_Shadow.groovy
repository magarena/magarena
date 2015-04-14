[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent.getEquippedCreature() &&
                    damage.isTargetPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        TARGET_CREATURE_CARD_FROM_GRAVEYARD
                    ),
                    MagicGraveyardTargetPicker.ReturnToHand,
                    this,
                    "PN gains 3 life and may\$ return target creature card\$ " +
                    "from his or her graveyard to his or her hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),3));
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new MagicRemoveCardAction(it,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
