def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Illusion);
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(MagicTargetChoice.CREATURE),
                MagicCopyTargetPicker.create(),
                this,
                "Put SN onto the battlefield. You may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield, " + 
                "except it's an Illusion in addition to its other types and it gains \"When this creature becomes the target of a spell or ability, sacrifice it.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent chosen ->
                    final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(
                        event.getCardOnStack(),
                        chosen.getCardDefinition()
                    );
                    game.doAction(action);
                    final MagicPermanent perm = action.getPermanent();
                    game.doAction(new MagicAddStaticAction(perm, ST));
                    game.doAction(new MagicAddTriggerAction(perm, MagicWhenTargetedTrigger.SacWhenTargeted));
                } as MagicPermanentAction);
            } else {
                game.doAction(new MagicPlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
