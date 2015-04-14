[
    new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return (equippedCreature.isValid() &&
                    equippedCreature == creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 4/4 white Angel creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("4/4 white Angel creature token with flying")
            ));
        }
    }
]
