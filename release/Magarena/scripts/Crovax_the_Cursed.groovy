[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice a creature?"),
                this,
                "PN may\$ sacrifice a creature. If PN does, he or she puts a +1/+1 counter on SN. "+
                "If PN doesn't, remove a +1/+1 counter from SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sacrifice = new MagicSacrificePermanentEvent(event.getSource(), SACRIFICE_CREATURE);
            if (event.isYes() && sacrifice.isSatisfied()) {
                game.addEvent(sacrifice);
                game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(), MagicCounterType.PlusOne, 1));
            } else {
                game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(), MagicCounterType.PlusOne, -1));
            }
        }
    }
]
