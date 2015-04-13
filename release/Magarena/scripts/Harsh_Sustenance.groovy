[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int X = cardOnStack.getController().getNrOfPermanents(MagicType.Creature);
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(X),
                this,
                "SN deals X damage to target creature or player\$ and PN gains X life, where X is the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPlayer player = event.getPlayer();
                final int X = player.getNrOfPermanents(MagicType.Creature);
                game.doAction(new MagicDealDamageAction(event.getSource(),it,X));
                game.doAction(new ChangeLifeAction(player,X));
            });
        }
    }
]
