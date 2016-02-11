[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
            return other.isLand() && other.isEnemy(permanent);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent other) {
            return new MagicEvent(
                permanent,
                other.getController(),
                this,
                "RN exiles the top two cards of his or her library and PN draws two cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.getRefPlayer().getLibrary().getCardsFromTop(2) each {
                game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.Exile));
            };
            game.doAction(new DrawAction(event.getPlayer(), 2));
        }
    }
]
