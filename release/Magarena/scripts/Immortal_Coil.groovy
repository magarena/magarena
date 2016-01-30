import magic.model.choice.MagicFromCardListChoice

def YouLoseAction = {
    final MagicGame game, final MagicEvent event ->
        game.doAction(new LoseGameAction(event.getPlayer(), " lost the game because of having no cards in his or her graveyard."));
}

def YouLoseEvent = {
    final MagicSource source ->
        return new MagicEvent(
            source,
            YouLoseAction,
            "PN loses the game."
        );
}

def exileCard = {
    final MagicGame game, final MagicEvent event ->
        event.processChosenCards(game, {
            final MagicCard chosen ->
                game.doAction(new ShiftCardAction(chosen, MagicLocationType.Graveyard, MagicLocationType.Exile));
                game.logAppendMessage(event.getPlayer(), "(${chosen.getName()})" )
        });
}

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return source.getController().getGraveyard().size() == 0;
        }

        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new PutStateTriggerOnStackAction(
                YouLoseEvent(source)
            ));
        }
    },

    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            final MagicPlayer player = permanent.getController();
            int amount = 0;

            if (target == player) {
                amount = damage.prevent();
            }

            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    target,
                    {
                        final MagicGame G, final MagicEvent E ->
                            final List<MagicCard> graveyard = player.getGraveyard();
                            G.addEvent(new MagicEvent(
                                E.getSource(),
                                player,
                                new MagicFromCardListChoice(graveyard, amount),
                                exileCard,
                                ""
                            ));

                    },
                    "Prevent ${amount} damage and PN exiles ${amount} cards from his or her graveyard."
                ) :
                MagicEvent.NONE;
        }
    }
]
