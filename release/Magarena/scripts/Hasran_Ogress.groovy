[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}")),
                ),
                this,
                "PN may pay\$ {2}\$. If not paid, SN deals 3 damage to PN."
            );
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
