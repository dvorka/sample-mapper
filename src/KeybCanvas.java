//---------------------------------------------------------------------------
//		      JNaaga - the game (java application)
//                              by Dvorka
//				   1999
//---------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;

class KeybCanvas extends Canvas
    implements MouseListener, MouseMotionListener
{
    static final int num         =11*7;     // number of keys
    static final int horizPadding=10;       // on L and R
    static final int vertPadding =10;       // only above

    static int thickness;
    static int keyHeight;

    int width;
    int height;

    // flags for halftones: pattern XX.XXX.
    boolean tripple; // switch when . comes
    int     halfoff; // 012 -> switch; 0123 -> switch

    Color elephantColor = new Color(0.98f, 0.97f, 0.85f);

    public String     newName=null;          // name of the new record



    public KeybCanvas()
    {
        addMouseListener(this);
        addMouseMotionListener(this);

        keyHeight =50;
        thickness =8;

        width  =num*thickness  +2*horizPadding;
        height =keyHeight      +vertPadding     +1;

        tripple= true;
        halfoff= 3;
    }


    public void paint(Graphics g)
    {
        int i;

        // get current size
        int w = getSize().width;
        int h = getSize().height;

        // recount parameters
        keyHeight = h -(vertPadding             +1);
        thickness =(w -2*horizPadding)/num;
        width  =num*thickness  +2*horizPadding;
        height =keyHeight      +vertPadding     +1;

        // draw keyboard
        g.setColor(Color.white);
        g.fillRect(0, 0, w-1, h);

        g.setColor(elephantColor);
        g.fillRect(horizPadding, vertPadding,
                   num*thickness, keyHeight);
        g.setColor(Color.black);
        g.drawRect(horizPadding, vertPadding,
                   num*thickness, keyHeight);
         g.drawLine(horizPadding,                       vertPadding+keyHeight-3,
                    horizPadding+num*thickness, vertPadding+keyHeight-3);
        for(i=0; i<num; i++)
        {
            g.drawLine(horizPadding+i*thickness+thickness,vertPadding+1,
                       horizPadding+i*thickness+thickness,vertPadding+keyHeight-1);

            // halftones
            if(tripple)
            {
                if(halfoff==3)
                 { tripple=false; halfoff=0; }
                else
                {
                    g.fillRect(horizPadding+i*thickness-thickness/4,vertPadding+1,
                               thickness/2,keyHeight/2);
                    halfoff++;
                }
            }
            else
            {
                if(halfoff==2)
                 { tripple=true; halfoff=0; }
                else
                {
                    g.fillRect(horizPadding+i*thickness-thickness/4,vertPadding+1,
                               thickness/2,keyHeight/2);
                    halfoff++;
                }
            }
        }
    }



    //If we don't specify this, the canvas might not show up at all
    //(depending on the layout manager).
    public Dimension getMinimumSize()
    {
        return new Dimension(getSize().width,height);
    }



    //If we don't specify this, the canvas might not show up at all
    //(depending on the layout manager).
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }



    // events
    public void mousePressed(MouseEvent e)
    {
        // select record or insert new record into record list
        boolean createNew=true;

        // check if is inside some record
        KeybRecord r;
        for(int i=0; i<SampleMapping.records.size(); i++ )
        {
            r=(KeybRecord)SampleMapping.records.elementAt(i);

            if(r.logFrom<=SMStatus.logNote && r.logTo>=SMStatus.logNote)
            {
                // ok inside some existing
                createNew=false;
                SampleMapping.comb.ar=r;
                SampleMapping.smStatus.repaint();
                // select it in list
                SampleMapping.textRecords.select(i+3);
                break;
            }
        }

        if(createNew)
        {
            // enter sample name and create new record
            EnterNameFrame.modify=false; // create new record (not modify)
            EnterNameFrame enf=new EnterNameFrame();
            enf.setTitle("Enter sample name");
            enf.pack();

            // now put the frame to the center of the total screen
            Dimension ddww = enf.getSize();   // get size of frame after pack
            // get size of screen
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            // move window to the center of global screen
            enf.setLocation(new Point((screen.width-ddww.width)/2,(screen.height-ddww.height)/2));
            enf.setVisible(true);
        }

        SampleMapping.comb.repaint();
    }

    public void mouseReleased(MouseEvent e)
    {
        if(false)
            System.out.println("Mouse released; # of clicks: "
                               + e.getClickCount());
    }

    public void mouseEntered(MouseEvent e)
    {
        if(false)
            System.out.println("Mouse entered");
    }

    public void mouseExited(MouseEvent e)
    {
        if(false)
            System.out.println("Mouse exited");
    }

    public void mouseClicked(MouseEvent e)
    {
        if(false)
            System.out.println("Mouse clicked (# of clicks: "
                               + e.getClickCount() + ")");
    }


    // mouse motion
    public void mouseMoved(MouseEvent e)
    {
        if(SampleMapping.mouseUpdateEnabled)
        {
            float notef=((float)e.getX() -(float)horizPadding)/(float)thickness;
            int   note =(int)notef;
            if(note<0)
                note=0;

            if(notef==((float)note))
                SampleMapping.smStatus.showNote(note, true); // check for cis,dis,...
            else
                SampleMapping.smStatus.showNote(note, false); // white key
        }
    }


    public void mouseDragged(MouseEvent e)
    {
        if(false)
            System.out.println("Mouse dragged; # of clicks: "+e.getClickCount());
    }

}

//- EOF -----------------------------------------------------------------------
