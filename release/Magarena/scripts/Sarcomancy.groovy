[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return game.getNrOfPermanents(MagicSubType.Zombie) == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If there are no Zombies on the battlefield, SN deals 1 damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (game.getNrOfPermanents(MagicSubType.Zombie) == 0) {
                final MagicDamage damage = new MagicDamage(event.getSource(),event.getPlayer(),1);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
