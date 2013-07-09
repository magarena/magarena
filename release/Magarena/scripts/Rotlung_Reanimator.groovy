[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent diedPermanent) {
            return (permanent == diedPermanent || diedPermanent.hasSubType(MagicSubType.Cleric)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 2/2 black Zombie creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("Zombie")
            ));
        }
    }
]
