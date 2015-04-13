[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            new MagicEvent(
                permanent,
                this,
                "SN gets +2/+0 until end of turn for each other attacking Goblin."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final int amount=game.filterPermanents(ATTACKING_GOBLIN.except(permanent)).size()*2;
            game.doAction(new ChangeTurnPTAction(permanent,amount,0));
        }
    }
]
