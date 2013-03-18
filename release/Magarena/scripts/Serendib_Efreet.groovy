[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPlayer player=permanent.getController();
            return (player==upkeepPlayer) ?
                new MagicEvent(
                        permanent,
                        this,
                        "SN deals 1 damage to PN.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicDamage damage=new MagicDamage(event.getSource(),event.getPlayer(),1);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
