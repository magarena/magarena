[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Exile target creature card\$ from a graveyard. "+
                "PN creates X 1/1 green Saproling creature tokens, where X is the exiled creature card's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final int amount = it.getToughness();
                final MagicPlayer player = event.getPlayer();
                game.logAppendValue(player,amount);
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.Exile));
                game.doAction(new PlayTokensAction(player,CardDefinitions.getToken("1/1 green Saproling creature token"),amount));
            });
        }
    }
]
