[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all cards from all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
                for (final MagicCard cardGraveyard : graveyard) {
                    game.doAction(new MagicRemoveCardAction(cardGraveyard,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(cardGraveyard,MagicLocationType.Graveyard,MagicLocationType.Exile));
                }
            }
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack,
                this,
                "Counter RN if a card with the same name is in a graveyard or a nontoken permanent with the same name is on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardOnStack spell = event.getRefCardOnStack();
            final String name = spell.getCard().getName();
            final int graveyard = game.filterCards(
                cardName(name)
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
            ).size();
            final int battlefield = game.filterPermanents(
                nonTokenPermanentName(
                    name, 
                    Control.Any
                )
            ).size();
            final int amount = graveyard + battlefield;
            if (amount > 0) {
                game.logAppendMessage(event.getPlayer(), "("+name+") is countered.")
                game.doAction(new CounterItemOnStackAction(spell));
            }
        }
    }
]
