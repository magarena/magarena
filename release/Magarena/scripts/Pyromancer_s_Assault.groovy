[
    new OtherSpellIsCastTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
            return permanent.isController(spell.getController()) && permanent.getController().getSpellsCast() == 1;
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                    permanent,
                    NEG_TARGET_CREATURE_OR_PLAYER,
                    this,
                    "SN deals 2 damage to target creature or player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,2));
            });
        }
    }
]
