def control = {
    final int you, final MagicTargetFilter<MagicPermanent> filter ->
    return new MagicStatic(MagicLayer.Control,filter) {
        @Override
        public MagicPlayer getController(
            final MagicPermanent source, 
            final MagicPermanent permanent, 
            final MagicPlayer player) {
            return source.getController();
        }
        @Override
        public boolean condition(
            final MagicGame game,
            final MagicPermanent source,
            final MagicPermanent target) {
            if (you != source.getController().getIndex()) {
                //remove this static after the update
                game.addDelayedAction(new MagicRemoveStaticAction(source, this));
                return false;
            } else {
                return true;
            }
        }
    }
}

[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.NEG_TARGET_CREATURE
                ),
                this,
                "PN may\$ gain control of target creature\$ for as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        final MagicPermanent source = event.getPermanent();
                        final MagicTargetFilter<MagicPermanent> filter = new MagicTargetFilter.MagicPermanentTargetFilter(perm);
                        game.doAction(new MagicAddStaticAction(source, control(source.getController().getIndex(), filter)));
                    };
                });
            }
        }
    }
]
