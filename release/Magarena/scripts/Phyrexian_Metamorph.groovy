def type = new MagicStatic(MagicLayer.Type) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags | MagicType.Artifact.getMask();
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(MagicTargetChoice.CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "You may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent chosen ->
                    final MagicPlayCardFromStackAction action = MagicPlayCardFromStackAction.EnterAsCopy(
                        event.getCardOnStack(),
                        chosen
                    );
                    game.doAction(action);
                    final MagicPermanent perm = action.getPermanent();
                    game.doAction(new MagicAddStaticAction(perm, type));

                });
            } else {
                game.logAppendMessage(event.getPlayer(), "Put ${event.getCardOnStack()} onto the battlefield.");
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
