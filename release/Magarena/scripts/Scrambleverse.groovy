[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "For each nonland permanent, choose a player at random. Then each player gains control of each permanent for which he or she was chosen. Untap those permanents."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicPermanent> pList = new MagicPermanentList();
            final int players = game.getPlayers().size();
            for(final MagicPlayer player : game.getPlayers()) {
                pList.addAll(player.getPermanents());
            }
            for(final MagicPermanent permanent : pList) {
                if(!permanent.hasType(MagicType.Land)) {
                    final int newCtrlNr = ((int)(Math.random()*Math.pow(players,2)))%players
                    game.doAction(new MagicGainControlAction(game.getPlayer(newCtrlNr), permanent, MagicStatic.Forever));
                    game.doAction(new MagicUntapAction(permanent));
                }
            }
        }
    }
]
