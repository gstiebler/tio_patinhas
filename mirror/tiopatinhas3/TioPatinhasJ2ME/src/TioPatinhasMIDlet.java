/* 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility. 
 */

import java.io.IOException;

import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;
import javax.microedition.midlet.MIDlet;

public class TioPatinhasMIDlet
    extends MIDlet
    implements CommandListener {
    
  private Display mDisplay;
  
  private Form mMainForm;
  
  private Command mExitCommand, mCameraCommand;
  private Command mBackCommand, mCaptureCommand;
  
  private Player mPlayer;
  private VideoControl mVideoControl;
  
  public TioPatinhasMIDlet() {
    mExitCommand = new Command("Exit", Command.EXIT, 0);
    mCameraCommand = new Command("Camera", Command.SCREEN, 0);
    mBackCommand = new Command("Back", Command.BACK, 0);
    mCaptureCommand = new Command("Capture", Command.SCREEN, 0);
    
    mMainForm = new Form("Tio Patinhas");
    mMainForm.addCommand(mExitCommand);
    String supports = System.getProperty("video.snapshot.encodings");
    if (supports != null && supports.length() > 0) {
      mMainForm.append("Ready to take pictures.");
      mMainForm.addCommand(mCameraCommand);
    }
    else
      mMainForm.append("Tio Patinhas cannot use this device to take pictures.");
    mMainForm.setCommandListener(this);
  }
  
  public void startApp() {
    mDisplay = Display.getDisplay(this);
    
    mDisplay.setCurrent(mMainForm);
  }
    
  public void pauseApp() {}
  
  public void destroyApp(boolean unconditional) {
  }
  
  public void commandAction(Command c, Displayable s) {
    if (c.getCommandType() == Command.EXIT) {
      destroyApp(true);
      notifyDestroyed();
    }
    else if (c == mCameraCommand)
      showCamera();
    else if (c == mBackCommand)
      mDisplay.setCurrent(mMainForm);
    else if (c == mCaptureCommand) {
      capture();
    }
  }

  private void showCamera() {
    try {
        System.out.print("Fudido.");
      mPlayer = Manager.createPlayer("capture://image");
      mPlayer.realize();
      
      mVideoControl = (VideoControl)mPlayer.getControl("VideoControl");
      
      Canvas canvas = new CameraCanvas(this, mVideoControl);
      canvas.addCommand(mBackCommand);
      canvas.addCommand(mCaptureCommand);
      canvas.setCommandListener(this);
      mDisplay.setCurrent(canvas);

      /*
      Form form = new Form("Camera form");
      Item item = (Item)mVideoControl.initDisplayMode(
          GUIControl.USE_GUI_PRIMITIVE, null);
      form.append(item);
      form.addCommand(mBackCommand);
      form.addCommand(mCaptureCommand);
      form.setCommandListener(this);
      mDisplay.setCurrent(form);
      */

      mPlayer.start();
    }
    catch (IOException ioe) { handleException(ioe); }
    catch (MediaException me) { handleException(me); }
  }
  
  public void capture() {
    try {
      // Get the image.
      byte[] raw = mVideoControl.getSnapshot(null);
      Image image = Image.createImage(raw, 0, raw.length);
      
      int [] ARGB = new int[image.getWidth()*image.getHeight()];
      
      // Getting the ARGB of the image into the RGB int array
      image.getRGB(ARGB, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
      ImageProcessor.initializeImage(image);
      ImageProcessor.process(image);
      
      Image thumb = createThumbnail(image);
      
      // Place it in the main form.
      if (mMainForm.size() > 0 && mMainForm.get(0) instanceof StringItem)
        mMainForm.delete(0);
      mMainForm.append(thumb);
      
      // Flip back to the main form.
      mDisplay.setCurrent(mMainForm);
      
      // Shut down the player.
      mPlayer.close();
      mPlayer = null;
      mVideoControl = null;
    }
    catch (MediaException me) { handleException(me); }
  }
  
  private void handleException(Exception e) {
    Alert a = new Alert("Exception", e.toString(), null, null);
    a.setTimeout(Alert.FOREVER);
    mDisplay.setCurrent(a, mMainForm);
  }
  
  private Image createThumbnail(Image image) {
    int sourceWidth = image.getWidth();
    int sourceHeight = image.getHeight();
    
    int thumbWidth = 64;
    int thumbHeight = -1;
    
    if (thumbHeight == -1)
      thumbHeight = thumbWidth * sourceHeight / sourceWidth;
    
    Image thumb = Image.createImage(thumbWidth, thumbHeight);
    Graphics g = thumb.getGraphics();
    
    for (int y = 0; y < thumbHeight; y++) {
      for (int x = 0; x < thumbWidth; x++) {
        g.setClip(x, y, 1, 1);
        int dx = x * sourceWidth / thumbWidth;
        int dy = y * sourceHeight / thumbHeight;
        g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
      }
    }
    
    Image immutableThumb = Image.createImage(thumb);
    
    return immutableThumb;
  }
}
