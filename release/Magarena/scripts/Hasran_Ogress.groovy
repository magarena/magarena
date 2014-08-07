[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{2}")),
                    ),
                    this,
                    "You may pay\$ {2}\$. If you don't, SN         deals 3 damage to you\$."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),3)
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
