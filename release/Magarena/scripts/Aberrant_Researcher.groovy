[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts the top card of his or her library into his or her graveyard. "+
                "If it's an instant or sorcery card, transform SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPermanent permanent = event.getPermanent();
            for (final MagicCard card : player.getLibrary().getCardsFromTop(1)) {
                game.doAction(new MillLibraryAction(player, 1));
                if (card.hasType(MagicType.Instant) || card.hasType(MagicType.Sorcery)) {
                    game.logAppendMessage(player,"${permanent.getName()} transforms.");
                    game.doAction(new TransformAction(permanent));
                }
            }
        }
    }
]
