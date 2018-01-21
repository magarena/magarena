[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "Destroy target creature\$ if it has converted mana cost 2 or less. " +
                "Destroy that creature if it has converted mana cost 4 or less instead if a permanent you controlled left the battlefield this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int maxCost = (event.getPlayer().hasState(MagicPlayerState.Revolt)) ? 4 : 2;
                if (it.getConvertedCost() <= maxCost) {
                    game.doAction(new DestroyAction(it));
                }
            });
        }
    }
]

