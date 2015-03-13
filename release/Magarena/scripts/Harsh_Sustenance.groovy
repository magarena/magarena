[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int X = cardOnStack.getController().getNrOfPermanents(MagicType.Creature);
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(X),
                this,
                "SN deals X damage to target creature or player\$ and PN gains X life, where X is the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int X = event.getPlayer().getNrOfPermanents(MagicType.Creature);
                final MagicDamage damage=new MagicDamage(event.getSource(),it,X);
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),X));
            });
        }
    }
]
