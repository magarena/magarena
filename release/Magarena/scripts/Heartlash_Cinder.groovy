[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "SN gets +X/+0 until end of turn, where X is the number of red mana symbols in the mana costs of permanents PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Red);
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),amount,0));
        }
    }
]
