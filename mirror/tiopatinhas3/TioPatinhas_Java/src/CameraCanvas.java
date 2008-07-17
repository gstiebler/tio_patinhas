/* 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility. 
 */


import javax.microedition.lcdui.*;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.VideoControl;

public class CameraCanvas
    extends Canvas {
  private TioPatinhasMIDlet mTioPatinhasMIDlet;
  
  public CameraCanvas(TioPatinhasMIDlet midlet, VideoControl videoControl) {
    int width = getWidth();
    int height = getHeight();
    
    mTioPatinhasMIDlet = midlet;
    
    videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
    try {
      videoControl.setDisplayLocation(2, 2);
      videoControl.setDisplaySize(width - 4, height - 4);
    }
    catch (MediaException me) {
      try { videoControl.setDisplayFullScreen(true); }
      catch (MediaException me2) {}
    }
    videoControl.setVisible(true);
  }
  
  public void paint(Graphics g) {
    int width = getWidth();
    int height = getHeight();

    // Draw a green border around the VideoControl.
    g.setColor(0x00ff00);
    g.drawRect(0, 0, width - 1, height - 1);
    g.drawRect(1, 1, width - 3, height - 3);
  }
  
  public void keyPressed(int keyCode) {
    int action = getGameAction(keyCode);
    if (action == FIRE)
      mTioPatinhasMIDlet.capture();
  }
}
