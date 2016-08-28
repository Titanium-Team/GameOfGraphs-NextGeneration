package graph;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

public class GraphPanel{

    private Graph graph;

    public GraphPanel() {
        if (view instanceof MapEditorView){
            graph = GameOfGraphs.getGame().getMapEditorController().getGraph();
        }


        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                double zoomFactor = - SCALE_STEP*e.getPreciseWheelRotation()*zoom;
                zoom = Math.abs(zoom + zoomFactor);

                Dimension d = new Dimension((int)(graph.getWidth()*zoom), (int)(graph.getHeight()*zoom));


                if (d.getWidth() <= 1263) {
                    d.setSize(1263, d.getHeight());
                    zoom = d.getWidth()/graph.getWidth();
                }

                if (d.getHeight() <= 523) {
                    d.setSize(d.getWidth(), 523);
                    zoom = d.getHeight()/graph.getHeight();
                }

                setPreferredSize(d);
                setSize(d);
                revalidate();

                Rectangle visibleRect = getVisibleRect();

                scrollX = e.getX() / previousZoom * zoom - (e.getX() - visibleRect.getX());
                scrollY = e.getY() / previousZoom * zoom - (e.getY() - visibleRect.getY());

                visibleRect.setRect(scrollX, scrollY, visibleRect.getWidth(), visibleRect.getHeight());
                scrollRectToVisible(visibleRect);

                previousZoom = zoom;

                repaint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if (origin != null) {
                    int deltaX = origin.x - e.getX();
                    int deltaY = origin.y - e.getY();
                    Rectangle view = getVisibleRect();
                    view.x += deltaX;
                    view.y += deltaY;
                    scrollRectToVisible(view);
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if ((view instanceof MapEditorView && GameOfGraphs.getGame().getMapEditorController().getChooser() == 2 || view instanceof MapEditorView == false) && e.getButton() == 1){
                    origin = new Point(e.getPoint());
                }else {
                    origin = null;
                }
            }
        });
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setJScrollPane(JScrollPane jScrollPane) {
        this.jScrollPane = jScrollPane;
    }

    public double getZoom() {
        return zoom;
    }
}
