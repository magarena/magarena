[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
            return new MagicEvent(
                source,
                source.getCard(),
                this,
                "Return RN to the battlefield flipped."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReturnCardAction(
                MagicLocationType.Graveyard,
                event.getRefCard(),
                event.getPlayer(),
                MagicPlayMod.FLIPPED
            ));
        }
    }
]
