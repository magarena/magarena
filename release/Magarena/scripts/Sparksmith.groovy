[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                this,
                "SN deals X damage to target creature\$ and X damage to PN, "+
                "where X is the number of Goblins in play."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final MagicPlayer player = event.getPlayer()
                final int X = game.getNrOfPermanents(MagicSubType.Goblin);
                final MagicDamage damage=new MagicDamage(event.getSource(),it,X);
                game.doAction(new MagicDealDamageAction(damage));
		final MagicDamage damage2=new MagicDamage(event.getSource(),player,X);
                game.doAction(new MagicDealDamageAction(damage2));
            });
        }
    }
]
