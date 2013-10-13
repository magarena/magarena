def COLORED_PERMANENT = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return (target.hasColor(MagicColor.Black) 
                || target.hasColor(MagicColor.Red) 
                || target.hasColor(MagicColor.White) 
                || target.hasColor(MagicColor.Green) 
                || target.hasColor(MagicColor.Blue));
        }
    };
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player sacrifices all colored permanents."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {               
                final Collection<MagicPermanent> targets =
                    game.filterPermanents(player, COLORED_PERMANENT);
                for (final MagicPermanent target : targets) {
                    game.doAction(new MagicSacrificeAction(target));
                }    
            }
        }
    }
]
