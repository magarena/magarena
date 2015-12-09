[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicCopyPermanentPicker.create(),
                this,
                "PN puts a token that's a copy of target creature\$ onto the battlefield. "+
                "That token has haste and \"At the beginning of the end step, exile this permanent.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    it,
                    [MagicPlayMod.HASTE_UEOT, MagicPlayMod.EXILE_AT_END_OF_TURN]
                ));
            });
        }
    }
]
