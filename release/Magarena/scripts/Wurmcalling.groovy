[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Put an ${x}/${x} green Wurm creature token onto the battlefield. " +
                "If the buyback cost was payed, return SN to its owner's hand as it resolves."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getCardOnStack().getX();
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("green Wurm creature token"),
                MagicPlayMod.PT(x,x)
            ));
            if (event.isBuyback()) {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
            }
        }
    }
]
