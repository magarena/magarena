[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.getCounters(MagicCounterType.Level)>0) {
                pt.set(6,6);
            }
        }
    },
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent==creature&&permanent.getCounters(MagicCounterType.Level)>=6) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals 6 damage to each creature defending player controls."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source=event.getPermanent();
            final MagicPlayer defendingPlayer=event.getPlayer().getOpponent();
            final Collection<MagicPermanent> creatures=
                game.filterPermanents(defendingPlayer,MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage=new MagicDamage(source,creature,6);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
