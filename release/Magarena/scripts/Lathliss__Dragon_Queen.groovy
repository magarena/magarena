
[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
            return played.isNonToken() &&
                played.hasSubType(MagicSubType.Dragon) &&
                played.isFriend(permanent);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent played) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a 5/5 red Dragon creature token with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("5/5 red Dragon creature token with flying")
            ));
        }
    }
]
