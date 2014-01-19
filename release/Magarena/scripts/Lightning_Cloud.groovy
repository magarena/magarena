[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.hasColor(MagicColor.Red)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{R}")),
                        MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER
                    ),
                    new MagicDamageTargetPicker(1),
                    this,
                    "PN may\$ pay {R}\$. If you do, SN deals 1 " +
                    "damage to target creature or player\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game, {
                    final MagicTarget target ->
                    final MagicDamage damage = new MagicDamage(event.getPermanent(),target,1);
                    game.doAction(new MagicDealDamageAction(damage));
                });
            }
        }
    }
]
