[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{X}{R}")),
                        MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER
                    ),
                    new MagicDamageTargetPicker(
                        permanent.getController().getMaximumX(game,MagicManaCost.create("{X}{R}"))
                    ),
                    this,
                    "PN may\$ pay {X}{R}\$. If PN does, SN deals X damage to target creature or player\$."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game, {
                    final MagicPayManaCostResult payedManaCost = event.getPaidMana();
                    final MagicDamage damage=new MagicDamage(event.getPermanent(),it,payedManaCost.getX());
                    game.doAction(new MagicDealDamageAction(damage));
                });
            }
        }
    }
]
