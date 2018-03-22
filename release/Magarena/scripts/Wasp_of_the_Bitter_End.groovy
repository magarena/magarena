def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new SacrificeAction(event.getPermanent()));
    game.doAction(new DestroyAction(event.getRefPermanent()));
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
                NEG_TARGET_CREATURE,
                this,
                "PN chooses a target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Sacrifice ${event.getSource()}?"),
                    it,
                    action,
                    "PN may\$ sacrifice SN. If PN does, destroy RN."
                ));
            });
        }
    }
]

