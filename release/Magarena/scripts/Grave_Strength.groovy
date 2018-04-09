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
            event.processTargetPermanent(game, {
                game.doAction(new MillLibraryAction(event.getPlayer(),3));
                final int amount = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
                game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.PlusOne,amount))
            });
        }
    }
]
