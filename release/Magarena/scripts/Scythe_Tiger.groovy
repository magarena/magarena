[
new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
        return new MagicEvent(
            permanent,
            new MagicMayChoice(MagicTargetChoice.SACRIFICE_LAND),
            MagicSacrificeTargetPicker.create(),
            this,
            "You may sacrifice a land. If you don't, sacrifice SN."
        );
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        if (event.isYes()) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent land) {
                    game.doAction(new MagicSacrificeAction(land));
                }
            });
        } else {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    }
}
]
