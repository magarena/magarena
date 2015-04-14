[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return (permanent.getEquippedCreature() == damage.getSource() &&
                    damage.isTargetPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        2,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "PN may\$ draw two cards. If you do, discard a card."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player = event.getPlayer();
                game.doAction(new DrawAction(player, 2));
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    player
                ));
            }
        }
    }
]
