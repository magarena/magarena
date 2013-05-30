def control = {
    final MagicTargetFilter<MagicPermanent> filter, final int you ->
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
    };
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_ARTIFACT,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target artifact\$ for as long as you control SN."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    final MagicPermanent source = event.getPermanent();
                    final MagicTargetFilter<MagicPermanent> filter = new MagicTargetFilter.MagicPermanentTargetFilter(perm);
                    final int you = source.getController().getIndex();
                    game.doAction(new MagicAddStaticAction(source, control(filter, you)));
                }
            });
        }
    }
]
