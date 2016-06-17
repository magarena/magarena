[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getTarget(),
                this,
                "PN puts X 0/1 black Insect creature tokens onto the battlefield, where X is RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefPermanent().getPower();
            game.logAppendValue(event.getPlayer(),amount);
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("0/1 black Insect creature token"),
                amount
            ));
        }
    }
]
