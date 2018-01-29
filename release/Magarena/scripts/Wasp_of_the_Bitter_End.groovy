def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new DestroyAction(it));
    }
}

[
    new OtherSpellIsCastTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
            return spell.isSpell(MagicType.Planeswalker) && spell.isSpell(MagicSubType.Bolas);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new EnqueueTriggerAction(new MagicEvent(
                    event.getSource(),
                    NEG_TARGET_CREATURE,
                    action,
                    "Destroy target creature\$."
                )));
            }
        }
    }
]

