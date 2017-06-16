package magic.ui.widget.duel.viewer;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.duel.viewerinfo.PermanentViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;

@SuppressWarnings("serial")
public class ImagePermanentsViewer extends JPanel {

    private static final int POSITION_SPACING = 60;
    private static final int HORIZONTAL_SPACING = 40;
    private static final int VERTICAL_SPACING = 30;

    private static final float CARD_WIDTH = (float) ImageSizePresets.getDefaultSize().width;
    private static final float CARD_HEIGHT = (float) ImageSizePresets.getDefaultSize().height;
    private static final float CARD_ASPECT_RATIO = CARD_WIDTH / CARD_HEIGHT;

    private final SwingGameController controller;
    private final boolean isTop;

    private List<ImagePermanentViewer> viewers;
    private Set<?> validChoices;

    public ImagePermanentsViewer(final SwingGameController controller) {
        this(controller, false);
    }

    public ImagePermanentsViewer(final SwingGameController controller, final boolean isTop) {
        this.controller = controller;
        this.isTop = isTop;

        setLayout(null);
        setOpaque(false);

        viewers=Collections.emptyList();
        validChoices=Collections.emptySet();
    }

    private static int divideIntoRows(final List<ImagePermanentViewer> cards, final int maxCardsPerRow, final int startingRow) {
        int numCardsThisRow = 0;
        int currentRow = startingRow;

        for (final ImagePermanentViewer card : cards) {
            if (numCardsThisRow + 1 > maxCardsPerRow) {
                // goto next row
                currentRow++;
                numCardsThisRow = 0;
            }

            numCardsThisRow++;
            card.setLogicalRow(currentRow);
        }

        return currentRow;
    }

    private int divideAllIntoRows(final List<ImagePermanentViewer> creatures, final List<ImagePermanentViewer> nonCreatures, final int maxCardsPerRow) {
        final List<ImagePermanentViewer> firstCards = (isTop) ? nonCreatures : creatures;
        final List<ImagePermanentViewer> secondCards = (isTop) ? creatures : nonCreatures;

        int currentRow = divideIntoRows(firstCards, maxCardsPerRow, 1);
        if (firstCards.size() > 0) { // creatures go in separate row from others
            currentRow++;
        }

        return divideIntoRows(secondCards, maxCardsPerRow, currentRow);
    }

    private int calculateAndSetPositions(final List<ImagePermanentViewer> creatures, final List<ImagePermanentViewer> nonCreatures) {
        int currentRow=1;
        int x=0;
        int y=0;
        int maxWidth=0;
        int rowHeight=0;

        final List<ImagePermanentViewer> aViewers = new ArrayList<ImagePermanentViewer>();
        if (isTop) {
            aViewers.addAll(nonCreatures);
            aViewers.addAll(creatures);
        } else {
            aViewers.addAll(creatures);
            aViewers.addAll(nonCreatures);
        }

        int prevPosition=aViewers.get(0).getPosition();

        for (final ImagePermanentViewer viewer : aViewers) {
            if (currentRow!=viewer.getLogicalRow()) {
                currentRow++;
                x=0;
                y+=VERTICAL_SPACING+rowHeight;
            }
            if (viewer.getPosition()!=prevPosition) {
                prevPosition=viewer.getPosition();
                if (x>0) {
                    x+=POSITION_SPACING;
                }
            }
            viewer.setLogicalPosition(new Point(x,y));
            final Dimension logicalSize=viewer.getLogicalSize();
            x+=logicalSize.width;
            maxWidth=Math.max(maxWidth,x);
            x+=HORIZONTAL_SPACING;
            rowHeight=Math.max(rowHeight,logicalSize.height);
        }

        final int maxHeight=y+rowHeight;
        final int width=getWidth();
        final int height=getHeight();
        int scaleMult=width;
        int scaleDiv=maxWidth;
        if ((maxHeight*scaleMult)/scaleDiv>height) {
            scaleMult=height;
            scaleDiv=maxHeight;
        }
        if (scaleMult>scaleDiv/2) {
            scaleMult=scaleDiv/2;
        }

        for (final ImagePermanentViewer viewer : aViewers) {
            final Point point=viewer.getLogicalPosition();
            viewer.setLocation((point.x*scaleMult)/scaleDiv,(point.y*scaleMult)/scaleDiv);
            final Dimension size=viewer.getLogicalSize();
            viewer.setSize((size.width*scaleMult)/scaleDiv,(size.height*scaleMult)/scaleDiv);
        }

        return (1000*scaleMult)/scaleDiv;
    }

    private void calculateOptimalPositions(final List<ImagePermanentViewer> creatures, final List<ImagePermanentViewer> nonCreatures) {
        final float screenWidth = (float) getWidth();
        final float screenHeight = (float) getHeight();
        final int numCards = creatures.size() + nonCreatures.size();

        if (numCards > 0 && screenWidth > 0 && screenHeight > 0) { // ignore cases where drawing doesn't matter
            int r;
            int maxCardsForBestNumRow = 1;
            float largestScaledCardSize = 0;

            // approximate number of rows needed to contain all the cards
            for (r = (creatures.isEmpty() || nonCreatures.isEmpty()) ? 1 : 2; r < numCards; r++) {
                float numCardsPerRow = (float) Math.ceil((float) numCards / r); // avoid lost of precision

                // max width and height for a card using this number of rows
                float scaledCardHeight = screenHeight / r;
                float scaledCardWidth = screenWidth / numCardsPerRow;

                // change width or height to maintain aspect ratio
                if (scaledCardWidth / scaledCardHeight > CARD_ASPECT_RATIO) {
                    // height is limiting factor on size of scaled card
                    scaledCardWidth = (scaledCardHeight / CARD_HEIGHT) * CARD_WIDTH;
                } else {
                    // width is limiting factor on size of scaled card
                    scaledCardHeight = (scaledCardWidth / CARD_WIDTH) * CARD_HEIGHT;
                }
                numCardsPerRow = (float) Math.ceil(screenWidth / scaledCardWidth); // scaled -> more cards can fit per row

                // set best possible
                final float scaledCardSize = scaledCardWidth * scaledCardHeight;
                if (scaledCardSize  > largestScaledCardSize) {
                    largestScaledCardSize = scaledCardSize;
                    maxCardsForBestNumRow = (int) numCardsPerRow;
                }
            }

            divideAllIntoRows(creatures, nonCreatures, maxCardsForBestNumRow);
            calculateAndSetPositions(creatures, nonCreatures);
        }
    }

    public void viewPermanents(final Collection<PermanentViewerInfo> permanentInfos) {
        final List<ImagePermanentViewer> creatures = new ArrayList<ImagePermanentViewer>();
        final List<ImagePermanentViewer> nonCreatures = new ArrayList<ImagePermanentViewer>();
        final List<ImagePermanentViewer> newViewers = new ArrayList<ImagePermanentViewer>();

        for (final PermanentViewerInfo permanentInfo : permanentInfos) {
            final ImagePermanentViewer perm = new ImagePermanentViewer(this,permanentInfo);
            if (permanentInfo.creature) {
                creatures.add(perm);
            } else {
                nonCreatures.add(perm);
            }
            newViewers.add(perm); // permanentInfos has a specific order
        }

        calculateOptimalPositions(creatures, nonCreatures);

        removeAll();
        for (final ImagePermanentViewer viewer : newViewers) {
            add(viewer);
        }
        viewers=newViewers;
        revalidate();
        repaint();
    }

    public SwingGameController getController() {
        return controller;
    }

    public void showValidChoices(final Set<?> aValidChoices) {
        this.validChoices=aValidChoices;
        for (final ImagePermanentViewer viewer : viewers) {
            viewer.redrawCachedImage();
        }
    }

    public boolean isValidChoice(final PermanentViewerInfo permanentInfo) {
        return validChoices.contains(permanentInfo.permanent);
    }

    ImagePermanentViewer getViewer(CardViewerInfo cardInfo) {
        for (final ImagePermanentViewer viewer : viewers) {
            if (viewer.permanentInfo.isEqualTo(cardInfo)) {
                return viewer;
            }
            for (final PermanentViewerInfo info : viewer.permanentInfo.linked) {
                if (info.isEqualTo(cardInfo)) {
                    return viewer;
                }
            }
            for (final PermanentViewerInfo info : viewer.permanentInfo.blockers) {
                if (info.isEqualTo(cardInfo)) {
                    return viewer;
                }
            }
        }
        return null;
    }

    void highlightCard(ImagePermanentViewer aViewer, long cardId) {
        if (aViewer != null) {
            aViewer.doShowHighlight(cardId);
        }
    }
}
