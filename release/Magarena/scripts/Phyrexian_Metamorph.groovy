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
                new MagicMayChoice(AN_ARTIFACT_OR_CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "You may\$ have SN enter the battlefield as a copy of any artifact or creature\$ on the battlefield, except it's an artifact in addition to its other types."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicEnterAsCopyAction(event.getCardOnStack(), it, {
                        final MagicPermanent perm ->
                        final MagicGame G = perm.getGame();
                        G.doAction(new MagicAddStaticAction(perm, type));
                    }));
                });
            } else {
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
