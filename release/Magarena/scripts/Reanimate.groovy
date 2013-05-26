[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                new MagicGraveyardTargetPicker(false), // no mana cost but payed with life
                this,
                "Put target creature card\$ from a graveyard onto the battlefield under your control. " +
                "PN loses life equal to its converted mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    final MagicPlayer player=event.getPlayer();
                    game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
                    game.doAction(new MagicChangeLifeAction(player,-targetCard.getCardDefinition().getConvertedCost()));
                }
            });
        }
    }
]
