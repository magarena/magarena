[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.ReturnToHand,
                this,
                "Put target creature card from a graveyard\$ onto the battlefield under PN's control. " +
                "PN loses life equal to its converted mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicPlayer player=event.getPlayer();
                game.doAction(new ReanimateAction(
                    it,
                    player
                ));
                game.doAction(new ChangeLifeAction(player,-it.getConvertedCost()));
            });
        }
    }
]
