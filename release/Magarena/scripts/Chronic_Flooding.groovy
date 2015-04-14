[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchantedLand = permanent.getEnchantedPermanent();
            return enchantedLand==tapped ?
                new MagicEvent(
                    permanent,
                    tapped.getController(),
                    this,
                    "PN puts the top three cards of his or her library into his or her graveyard."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new MillLibraryAction(event.getPlayer(),3));
        }
    }
]
