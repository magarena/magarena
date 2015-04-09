[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        NEG_TARGET_PLAYER
                    ),
                    this,
                    "PN may\$ have target player\$ lose life " +
                    "equal to the number of Allies he or she controls."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPlayer(game, {
                    final MagicPlayer player = event.getPlayer();
                    final int amount = player.getNrOfPermanents(MagicSubType.Ally);
                    game.doAction(new MagicChangeLifeAction(it,-amount));
                });
            }
        }
    }
]
