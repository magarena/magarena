[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{X}{R}")),
                    NEG_TARGET_CREATURE_OR_PLAYER
                ),
                new MagicDamageTargetPicker(
                    permanent.getController().getMaximumX(game,MagicManaCost.create("{X}{R}"))
                ),
                this,
                "PN may\$ pay {X}{R}\$. If paid, SN deals X damage to target creature or player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game, {
                    final MagicPayManaCostResult payedManaCost = event.getPaidMana();
                    game.doAction(new DealDamageAction(event.getPermanent(),it,payedManaCost.getX()));
                });
            }
        }
    }
]
