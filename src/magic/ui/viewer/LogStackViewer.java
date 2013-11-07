package magic.ui.viewer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class LogStackViewer extends TexturedPanel implements IStackViewerListener  {

    private final JLabel dummyLabel = new JLabel();
    private final boolean isLogAutosizeMode = true;
    private final JPanel logStackPanel = new JPanel();
//    private final JPanel stackContainer;
//    private final JSplitPane splitter;
//    private final JPanel splitterContainer;

    public LogStackViewer() {
        setBorder(FontsAndBorders.BLACK_BORDER);
    }

    @Override
    public void stackViewerUpdated() {
//        System.out.println("GamePanel.stackViewerUpdated : height = " + imageStackViewer.getHeight());
//        setLeftSideLayout();
//        lhsPanel.revalidate();
//        lhsPanel.repaint();
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
