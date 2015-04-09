[
    new MagicSpellCardEvent() {

        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                amount,
                this,
                "Destroy each nonland permanent with converted mana cost RN or less."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefInt();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(
                    event.getSource().getController(),
                    new MagicCMCPermanentFilter(
                        NONLAND_PERMANENT,
                        Operator.LESS_THAN_OR_EQUAL,
                        amount
                    )
                );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
