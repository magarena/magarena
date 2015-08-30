[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent.isFriend(creature)) ?
                new MagicEvent(
                    permanent,
                    creature,
                    this,
                    "RN gets it gets +1/+0 until end of turn for each other attacking creature that shares a creature type with it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPermanent creature=event.getRefPermanent();

            final Set<MagicSubType> self = new HashSet<MagicSubType>();
            for (final MagicSubType st: MagicSubType.ALL_CREATURES) {
                if (creature.hasSubType(st)) {
                    self.add(st);
                }
            }

            final List<MagicPermanent> attacking = ATTACKING_CREATURE_YOU_CONTROL.except(creature).filter(player);
            int amount = 0;
            for (final MagicPermanent attacker : attacking) {
                for (final MagicSubType st : self) {
                    if (attacker.hasSubType(st)) {
                        amount++;
                        break;
                    }
                }
            }

            if (amount > 0) {
                game.logAppendValue(player, amount);
                game.doAction(new ChangeTurnPTAction(creature,amount,0));
            }
        }
    }
]
