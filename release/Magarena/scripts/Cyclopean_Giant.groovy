def effect = MagicRuleEventAction.create("target land becomes a Swamp.")


[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                this,
                "Exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ShiftCardAction(
                event.getPermanent().getCard(),
                MagicLocationType.Graveyard,
                MagicLocationType.Exile
            ));
                game.addEvent(effect.getEvent(event));
        }
    }
]

