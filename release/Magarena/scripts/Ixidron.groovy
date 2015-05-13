[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicPlayer player = permanent.getController();
            NONTOKEN_CREATURE.except(permanent).filter(player) each {
                game.doAction(ChangeStateAction.Set(it, MagicPermanentState.FaceDown))
            }
            game.logAppendMessage(player,player.getName()+" turns all other nontoken creatures face down.");
            return MagicEvent.NONE;
        }
    },
    
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = FACE_DOWN_CREATURE.filter(player).size();
            pt.set(amount, amount);
        }
    }
]
