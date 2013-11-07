package magic.ui.viewer;

import net.miginfocom.swing.MigLayout;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class LogStackViewer extends TexturedPanel implements IStackViewerListener  {

    private final LogBookViewer logBookViewer;
    private final StackViewer stackViewer;

//    private final JLabel dummyLabel = new JLabel();
//    private final boolean isLogAutosizeMode = true;
//    private final JPanel logStackPanel = new JPanel();
//    private final JPanel stackContainer;
//    private final JSplitPane splitter;
//    private final JPanel splitterContainer;

    public LogStackViewer(LogBookViewer logBookViewer0, StackViewer stackViewer0) {

        logBookViewer = logBookViewer0;
        logBookViewer.setOpaque(false);

        stackViewer = stackViewer0;
        stackViewer.addListener(this);

        setBorder(FontsAndBorders.BLACK_BORDER);

    }

    @Override
    public void stackViewerUpdated() {
        System.out.println("LogStackViewer.stackViewerUpdated : " + stackViewer.getPreferredSize());
        setLogStackLayout();
        revalidate();
        logBookViewer.forceVerticalScrollbarToMax();
    }

    /**
     * The log should expand or contract depending on the height of the stack.
     * If the stack would cause the log to shrink below 60 pixels then stack
     * will cease expanding and use a vertical scrollbar instead.
     */
    public void setLogStackLayout() {

        int viewerHeight = getSize().height;
        int stackHeight = stackViewer.getPreferredSize().height + 1;
        int logHeight = 60; // minimum log height.

        if (stackHeight > 0) {
            if (viewerHeight - stackHeight < logHeight) {
                stackHeight = viewerHeight - logHeight;
            } else {
                logHeight = viewerHeight - stackHeight;
            }
            String rowConstraints = "[" + logHeight + "][" + stackHeight + "]";

            removeAll();
            setLayout(new MigLayout("insets 0, gap 0, flowy", "", rowConstraints));

            add(logBookViewer, "w 100%, h 100%");
            add(stackViewer, "w 100%, h 100%");
        }

    }

//  private void setSplitterLayout() {
//  setLogStackPanelLayout();
//  splitter.setTopComponent(new AlphaContainer(logStackPanel));
//  splitter.setBottomComponent(dummyLabel);
//}

//private void setLogStackPanelLayout() {
//
//  System.out.println("GamePanel.setLogStackPanelLayout");
//  System.out.print("-> logStackPanel pref=" + logStackPanel.getPreferredSize().height);
//  System.out.println(", size=" + logStackPanel.getSize().height);
//  System.out.print("-> logBookViewer pref=" + logBookViewer.getPreferredSize().height);
//  System.out.println(", size=" + logBookViewer.getSize().height);
//  System.out.print("-> imageStackViewer pref=" + imageStackViewer.getPreferredSize().height);
//  System.out.println(", size=" + imageStackViewer.getSize().height);
//
////  stackContainer.add(imageStackViewer, "w 100%, pushy, bottom");
////  stackContainer.setMinimumSize(imageStackViewer.getMinimumSize());
////  stackContainer.setPreferredSize(imageStackViewer.getPreferredSize());
////  stackContainer.setMaximumSize(imageStackViewer.getMaximumSize());
//
//  logStackPanel.removeAll();
//  logStackPanel.setLayout(new BorderLayout(0, 0)); // new MigLayout("insets 0, gap 0, flowy"));
//  // p.setBackground(Color.MAGENTA);
//  logStackPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
//  logStackPanel.setBackground(new Color(255, 255, 255, 200));
//
//  logStackPanel.setMinimumSize(new Dimension(300, 40));
//  int h1 = logStackPanel.getSize().height;
//
//
//
//  logBookViewer.setMinimumSize(new Dimension(300, 20));
//
////  int h2 = imageStackViewer.stackHeight;
////  if (h1 - h2 < 20) {
////      h2 = h1-20;
////  }
//
////  imageStackViewer.setPreferredSize(new Dimension(300, h2 + 1));
//  imageStackViewer.setMinimumSize(new Dimension(300, 21));
//  //imageStackViewer.setMaximumSize(new Dimension(300, imageStackViewer.stackHeight + 1));
//
//
//  //int h = lhsPanel.getHeight() - imageStackViewer.getPreferredSize().height;
//  logStackPanel.add(logBookViewer, BorderLayout.CENTER); //  "w 100%, hmin 20"); //, h " + h + "!");
//  logStackPanel.add(imageStackViewer, BorderLayout.SOUTH); //  "w 100%, hmin 20");
//
//}

}
