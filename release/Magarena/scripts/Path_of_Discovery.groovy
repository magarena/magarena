[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
            return played.hasType(MagicType.Creature) && permanent.isFriend(played);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
            return new MagicEvent(
                permanent,
                played,
                this,
                "RN explores."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicExploreEvent(event.getRefPermanent()));
        }
    }
]

