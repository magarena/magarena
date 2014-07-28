def TapTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return new MagicEvent(
            permanent,
            MagicTargetChoice.TARGET_PERMANENT_YOU_CONTROL,
            MagicTapTargetPicker.Untap,
            this,
            "PN untaps target permanent\$ PN controls."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game, {
            game.doAction(new MagicUntapAction(it));
        });
    }
}

def UntapTrigger = new MagicWhenComesIntoPlayTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        return new MagicEvent(
            permanent,
            MagicTargetChoice.TARGET_PERMANENT_AN_OPPONENT_CONTROLS,
            MagicTapTargetPicker.Tap,
            this,
            "PN taps target permanent\$ an opponent controls."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPermanent(game, {
            game.doAction(new MagicTapAction(it, true));
        });
    }
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    MagicTargetChoice.TARGET_PERMANENT_YOU_CONTROL,
                    MagicTargetChoice.TARGET_PERMANENT_AN_OPPONENT_CONTROLS
                ),
                payedCost,
                this,
                "Choose one\$ - Untap target permanent you control; or tap target permanent an opponent controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.executeTrigger(UntapTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            } else if (event.isMode(2)) {
                game.executeTrigger(TapTrigger, event.getPermanent(), event.getSource(), event.getRefPayedCost());
            }
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    }
]
