def PutBackAction = {
    final MagicGame game, final MagicEvent event ->
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.BottomOfOwnersLibrary));
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player shuffles all permanents he or she owns into his or her library, then reveals that many cards from the top of his or her library. Each player puts all artifact, creature, and land cards revealed this way onto the battlefield, then does the same for enchantment cards, then puts all cards revealed this way that weren't put onto the battlefield on the bottom of his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            /** STEP 1: Each player counts the number of permanents he or she owns
            STEP 2: Each player shuffles those permanents into his or her library.*/
            final MagicPermanentList playerPermList = new MagicPermanentList();
            final MagicPermanentList oppPermList = new MagicPermanentList();
            final MagicCardList playerCardList = new MagicCardList(); 
            final MagicCardList oppCardList = new MagicCardList(); 
            final MagicPlayer player = event.getPlayer();
            final MagicPlayer opp = event.getPlayer().getOpponent();
            final int playerCount;
            final int oppCount;

            playerPermList.addAll(player.getPermanents());
            playerCount = playerPermList.size();
            for(final MagicPermanent permanent : playerPermList) {
                game.doAction(new MagicRemoveFromPlayAction(permanent, MagicLocationType.TopOfOwnersLibrary));
            }
            player.getLibrary().shuffle();
            
            oppPermList.addAll(opp.getPermanents());
            oppCount = oppPermList.size();
            for(final MagicPermanent permanent : oppPermList) {
                game.doAction(new MagicRemoveFromPlayAction(permanent, MagicLocationType.TopOfOwnersLibrary));
            }
            opp.getLibrary().shuffle();
            
            /** END STEP 1+2
            STEP 3: Each player reveals cards from the top of his or her library equal to the number that player counted.*/
            
            final int playerAmount = (player.getLibrary().size() >= playerCount) ? playerCount : player.getLibrary().size();
            playerCardList.addAll(player.getLibrary().getCardsFromTop(playerAmount));
            for(final MagicCard card : playerCardList) {
                game.logAppendMessage(player, " "+player+" reveals "+card+". ");
            }
                        
            final int oppAmount = (opp.getLibrary().size() >= oppCount) ? oppCount : opp.getLibrary().size();
            oppCardList.addAll(opp.getLibrary().getCardsFromTop(oppAmount));
            for(final MagicCard card : oppCardList) {
                game.logAppendMessage(opp, " "+opp+" reveals "+card+". ");
            }

            /** END STEP 3
            STEP 4: Each player puts all artifact, land, and creature cards revealed this way onto the battlefield. All of these cards enter the battlefield at the same time. */
            
            for(int i = playerCardList.size() - 1; i >= 0; i--) {
                final MagicCard card = playerCardList.get(i);
                if(card.hasType(MagicType.Artifact) || card.hasType(MagicType.Land) || card.hasType(MagicType.Creature)) {
                    game.doAction(new MagicPlayCardAction(card, player));
                    playerCardList.remove(card);
                    player.getLibrary().remove(card);
                    game.logAppendMessage(player, " "+player+" puts "+card+" onto battlefield. ");
                    card.reveal();
                }
            }
            
            for(int i = oppCardList.size() - 1; i >= 0; i--) {
                final MagicCard card = oppCardList.get(i);
                if(card.hasType(MagicType.Artifact) || card.hasType(MagicType.Land) || card.hasType(MagicType.Creature)) {
                    game.doAction(new MagicPlayCardAction(card, opp));
                    oppCardList.remove(card);
                    opp.getLibrary().remove(card);
                    game.logAppendMessage(opp, " "+opp+" puts "+card+" onto battlefield. ");
                    card.reveal();
                }
            }
                
            /** END STEP 4
            STEP 5: Each player puts all enchantment cards revealed this way onto the battlefield. An Aura put onto the battlefield this way can enchant an artifact, land, 
            or creature that was already put onto the battlefield, but can't enchant an enchantment that's being put onto the battlefield at the same time as it. If multiple 
            players have Auras to put onto the battlefield, the player whose turn it is announces what his or her Auras will enchant, then each other player in turn order does 
            the same, then all enchantments (both Auras and non-Auras) enter the battlefield at the same time. */
            
            /** no idea how to handle the Auras */
            
            for(int i = playerCardList.size() - 1; i >= 0; i--) {
                final MagicCard card = playerCardList.get(i);
                if(card.hasType(MagicType.Enchantment) && !card.hasSubType(MagicSubType.Aura)) {
                    game.doAction(new MagicPlayCardAction(card, player));
                    playerCardList.remove(card);
                    player.getLibrary().remove(card);
                    game.logAppendMessage(player, " "+player+" puts "+card+" onto battlefield. ");
                    card.reveal();
                }
            }
            
            for(int i = oppCardList.size() - 1; i >= 0; i--) {
                final MagicCard card = oppCardList.get(i);
                if(card.hasType(MagicType.Enchantment) && !card.hasSubType(MagicSubType.Aura)) {
                    game.doAction(new MagicPlayCardAction(card, opp));
                    oppCardList.remove(card);
                    opp.getLibrary().remove(card);
                    game.logAppendMessage(opp, " "+opp+" puts "+card+" onto battlefield. ");
                    card.reveal();
                }
            }
            
            
            /** END STEP 5
            STEP 6: Each player puts all of his or her other revealed cards (instants, sorceries, planeswalkers, and Auras that can't enchant anything) on the bottom of his or her library in any order. */

            if(playerCardList.size() > 1) {
                game.addFirstEvent(new MagicEvent(
                    event.getSource(),
                    player,
                    new MagicFromCardListChoice(playerCardList, playerCardList.size()),
                    PutBackAction,
                    ""
                ));   
            }
            
            if(oppCardList.size() > 1) {
                game.addFirstEvent(new MagicEvent(
                    event.getSource(),
                    opp,
                    new MagicFromCardListChoice(oppCardList, oppCardList.size()),
                    PutBackAction,
                    ""
                ));   
            }
            /** END STEP 6 END CARD */
        }
    }
]
