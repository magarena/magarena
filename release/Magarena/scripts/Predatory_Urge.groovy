def ability = new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Fight"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to target creature\$. "+
                "That creature deals damage equal to its power to SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            final int power = creature.getPower();
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(creature, it, power));
                game.logAppendMessage(event.getPlayer(), MagicMessage.format("%s deals %s damage to %s", creature, power, it));
                game.doAction(new DealDamageAction(it, creature, it.getPower()));
                game.logAppendMessage(event.getPlayer(), MagicMessage.format("%s deals %s damage to %s", it, it.getPower(), creature));
            });
        }
    }

[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(ability);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
