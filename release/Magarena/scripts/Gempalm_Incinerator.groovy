[
    new MagicWhenCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
            final int X = card.getGame().getNrOfPermanents(MagicSubType.Goblin);
            return new MagicEvent(
                card,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(X),
                this,
                "When you cycle SN, you may have it deal X damage to target creature\$, "+
                "where X is the number of Goblins on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final int X = game.getNrOfPermanents(MagicSubType.Goblin);
                final MagicDamage damage=new MagicDamage(event.getSource(),it,X);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
