[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Target creature\$ gets +2/+0 until end of turn. " +
                "If PN controls a Huatli planeswalker, that creature gets +4/+0 until end of turn instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int powerUp = (event.getPlayer().controlsPermanent(planeswalker(MagicSubType.Huatli, Control.You))) ? 4 : 2;
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it, powerUp, 0));
            });
        }
    }
]

