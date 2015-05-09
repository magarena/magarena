[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent died) {
            final MagicPermanent equipped = permanent.getEquippedCreature();
            return (equipped == died &&
                    equipped.hasSubType(MagicSubType.Human)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 1/1 white Spirit creature " +
                    "token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new PlayTokenAction(
                player,
                CardDefinitions.getToken("1/1 white Spirit creature token with flying")
            ));
        }
    }
]
