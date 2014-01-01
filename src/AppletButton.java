/*
 * 1.1 version.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.Applet;

public class AppletButton extends Applet
                          implements Runnable, 
			             ActionListener {
    int frameNumber = 1;
    String windowClass;
    String buttonText;
    String windowTitle;
    int requestedWidth = 0;
    int requestedHeight = 0;
    Button button;
    Thread windowThread;
    Label label;
    boolean pleaseShow = false;
    boolean shouldInitialize = true;
    Class windowClassObject;

    public void init() {
	//Look up the parameters we need right away.
        windowClass = getParameter("WINDOWCLASS");
        if (windowClass == null) {
            windowClass = "TestWindow";
        }
        buttonText = getParameter("BUTTONTEXT");
        if (buttonText == null) {
            buttonText = "Click here to bring up a " + windowClass;
        }

	//Set up the button this applet displays.
        setLayout(new GridLayout(2,0));
        add(button = new Button(buttonText));
        button.setFont(new Font("Helvetica", Font.PLAIN, 14));
	button.addActionListener(this);
        add(label = new Label("", Label.CENTER));
    }

    public void start() {
        if (windowThread == null) {
            windowThread = new Thread(this, "Bringing Up " + windowClass);
            windowThread.start();
        }
    }

    public void stop() {
	windowThread = null;
    }

    public synchronized void run() {
	Object object = null;
	Frame window = null;
        String name = null;
        
        if (shouldInitialize) {
	    //Look up the rest of the parameters.
            windowTitle = getParameter("WINDOWTITLE");
            if (windowTitle == null) {
                windowTitle = windowClass;
	    }
            String windowWidthString = getParameter("WINDOWWIDTH");
            if (windowWidthString != null) {
                try {
                    requestedWidth = Integer.parseInt(windowWidthString);
                } catch (NumberFormatException e) {
                    //Use default width.
                }
	    }
            String windowHeightString = getParameter("WINDOWHEIGHT");
            if (windowHeightString != null) {
                try {
                    requestedHeight = Integer.parseInt(windowHeightString);
                } catch (NumberFormatException e) {
                    //Use default height.
		}
            }
 
            // Make sure the window class exists.
            try {
                windowClassObject = Class.forName(windowClass);
            } catch (Exception e) {
                // The specified class isn't anywhere that we can find.
                label.setText("Bad parameter: Couldn't find class "
                              + windowClass);
                button.setEnabled(false);
                return;
            }
    
	    // Create an invisible instance.
            window = createWindow(windowTitle);
	    if (window == null) {
	        return;
	    }

	    shouldInitialize = false;
        }

	Thread currentThread = Thread.currentThread();
        while (currentThread == windowThread) {

            //Wait until we're asked to show a window.
            while (pleaseShow == false) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            //We've been asked to bring up a window.
            pleaseShow = false;

	    //Create another window if necessary.
	    if (window == null) { 
		window = createWindow(windowTitle + ": " 
				      + ++frameNumber);
            } 

            window.setVisible(true);
            label.setText("");
	    window = null;
        } //end thread loop
    }

    private Frame createWindow(String title) {
	Object object = null;
	Frame window = null;

	//Instantiate the window class.
	try {
	    object = windowClassObject.newInstance(); 
	} catch (Exception e) {
	    label.setText("Bad parameter: Can't instantiate "
			  + windowClassObject);
	    button.setEnabled(false);
	    return null;
	}

	//Make sure it's a frame.
	try {
            window = (Frame)object;
        } catch (Exception e) {
            label.setText("Bad parameter: "
                          + windowClassObject +
                          " isn't a Frame subclass.");
            button.setEnabled(false);
            return null;
	}

        window.setTitle(title);

        //Set its size.
	window.pack();
        if ((requestedWidth > 0) 
          | (requestedHeight > 0)) {
            window.setSize(Math.max(requestedWidth,
                                    window.getSize().width),
                           Math.max(requestedHeight,
                                    window.getSize().height));
        }

	return window;
    }
                
    /* Signal the window thread to build a window. */
    public synchronized void actionPerformed(ActionEvent event) {
        label.setText("Please wait while the window comes up...");
        pleaseShow = true;
        notify();
    }
}

class TestWindow extends Frame {
    public TestWindow() {
	addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		dispose();
	    }
	});
    }
}
