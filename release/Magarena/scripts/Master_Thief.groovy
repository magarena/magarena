[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_ARTIFACT,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target artifact\$ for as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent perm ->
                game.doAction(new MagicAddStaticAction(
                    event.getPermanent(), 
                    MagicStatic.ControlAsLongAsYouControlSource(
                        event.getPlayer(),
                        perm
                    )
                ));
            } as MagicPermanentAction);
        }
    }
]
