def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Ninja);
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(A_CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "Put SN onto the battlefield. You may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield, " + 
                "except it's a Ninja in addition to its other types.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new EnterAsCopyAction(event.getCardOnStack(), it, {
                        final MagicPermanent perm ->
                        final MagicGame G = perm.getGame();
                        G.doAction(new AddStaticAction(perm, ST));
                    }));
                });
            } else {
                game.doAction(new PlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
