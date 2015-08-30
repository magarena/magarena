[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Exile target creature card from a graveyard.\$ PN gains life equal to that card's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final int amount = it.getToughness();
                final MagicPlayer player = event.getPlayer();
                game.logAppendValue(player,amount);
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.Exile));
                game.doAction(new ChangeLifeAction(player, amount));
            });
        }
    }
]
