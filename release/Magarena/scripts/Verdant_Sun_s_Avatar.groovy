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
                "PN gains life equal to RN's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), event.getRefPermanent().getToughness()));
        }
    }
]

