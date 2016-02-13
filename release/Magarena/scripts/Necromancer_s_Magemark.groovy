[
    new MagicStatic(MagicLayer.ModPT, CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(1, 1);
        }

        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.isEnchanted();
        }
    },
    new WouldBeMovedTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            final MagicPermanent dying = act.permanent;
            if (permanent.isFriend(dying) &&
                dying.isCreature() &&
                dying.isEnchanted() &&
                act.to(MagicLocationType.Graveyard) &&
                act.from(MagicLocationType.Battlefield)) {
                act.setToLocation(MagicLocationType.OwnersHand);
                game.logAppendMessage(permanent.getController(), "${dying.getName()} is put into ${dying.getController()}'s hand.");

            }
            return MagicEvent.NONE;
        }
    }
]
