[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final MagicTargetFilter<MagicPermanent> witch = new MagicNameTargetFilter("Bogbrew Witch");
            final int amount = (permanent.getController().controlsPermanent(witch))? 4: 1;
            return new MagicEvent(
                permanent,
                new MagicTargetChoice("target creature an opponent controls"),
                new MagicWeakenTargetPicker(amount,amount),
                this,
                "Target creature an opponent controls\$ gets -1/-1 until end of turn. "+
                "That creature gets -4/-4 instead if PN controls a creature named Bogbrew Witch."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicTargetFilter<MagicPermanent> witch = new MagicNameTargetFilter("Bogbrew Witch");
                final int amount = (event.getPlayer().controlsPermanent(witch))? 4: 1;
                game.doAction(new ChangeTurnPTAction(it, -amount, -amount));
            });
        }
    }
]
