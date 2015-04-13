[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "PN puts the top three cards of his or her library into his or her graveyard,"+
                "then puts a +1/+1 counter on target creature\$ for each creature card in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer(),3));
                event.processTargetPermanent(game, {
                final int amount = game.filterCards(event.getPlayer(), CREATURE_CARD_FROM_GRAVEYARD).size();
                    game.doAction(new ChangeCountersAction(it,MagicCounterType.PlusOne,amount))
            });
        }
    }
]
