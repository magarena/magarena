[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int X = cardOnStack.getGame().getNrOfPermanents(MagicSubType.Cleric);
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(X),
                this,
                "SN deals X damage to target creature or player\$ and PN gains X life, where X is the number of Clerics on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = game.getNrOfPermanents(MagicSubType.Cleric);
                game.logAppendMessage(event.getPlayer()," (X="+amount+")");
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
                game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
            });
        }
    }
]
