[
    new AtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer player) {
            return permanent.isController(player) && player.hasState(MagicPlayerState.CitysBlessing);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                "PN reveals the top card of PN's library and put it into PN's hand. " +
                "Each of PN's opponents loses X life and PN gains X life, " +
                "where X is that card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            player.getLibrary().getCardsFromTop(1).each {
                game.doAction(new RevealAction(it));
                game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
                final int amount = it.getConvertedCost();
                game.doAction(new ChangeLifeAction(player.getOpponent(), -amount));
                game.doAction(new ChangeLifeAction(player, amount));
            }
        }
    }
]

