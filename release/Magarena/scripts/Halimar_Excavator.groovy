[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_PLAYER,
                    this,
                    "Target player\$ puts the top X cards of his or her " +
                    "library into his or her graveyard, where X is the " +
                    "number of Allies PN controls."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = player.getNrOfPermanents(MagicSubType.Ally);
                game.doAction(new MagicMillLibraryAction(it,amount));
            });
        }
    }
]
