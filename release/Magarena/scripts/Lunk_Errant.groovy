[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent && game.getAttackingPlayer().getNrOfAttackers()==1) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+1 and gains trample until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),1,1));
            game.doAction(new GainAbilityAction(event.getPermanent(),MagicAbility.Trample));
        }
    }
]
