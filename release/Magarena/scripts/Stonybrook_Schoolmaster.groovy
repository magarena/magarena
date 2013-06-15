[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            return (permanent == tapped) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 1/1 blue Merfolk Wizard creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Merfolk Wizard")));
        }
    }
]
