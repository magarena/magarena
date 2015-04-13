def action = {
    final MagicGame game, final MagicEvent event ->
        game.doAction(new MagicChangeTurnPTAction(event.getRefPermanent(),0,3));
        game.doAction(new ChangeLifeAction(event.getPlayer(),1));
}

def event = {
    final MagicPermanent permanent, final MagicPermanent enchanted ->
    return new MagicEvent(
        permanent,
        enchanted,
        action,
        enchanted.toString()+" gets +0/+3 and PN gains a life."
    );
}

[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (creature == enchantedCreature) ?  event(permanent, enchantedCreature) : MagicEvent.NONE;
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (creature == enchantedCreature) ? event(permanent, enchantedCreature) : MagicEvent.NONE;
        }
    }
]
