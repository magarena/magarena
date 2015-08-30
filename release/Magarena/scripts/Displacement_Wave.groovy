[
    new MagicSpellCardEvent() {

        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getX(),
                this,
                "Return all nonland permanents with converted mana cost X or less to their owners' hands. (X=RN)"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveAllFromPlayAction(
                new MagicCMCPermanentFilter(
                    NONLAND_PERMANENT,
                    Operator.LESS_THAN_OR_EQUAL,
                    event.getRefInt()
                ).filter(event),
                MagicLocationType.OwnersHand
            ));
        }
    }
]
