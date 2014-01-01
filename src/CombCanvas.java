//---------------------------------------------------------------------------
//		      JNaaga - the game (java application)
//                              by Dvorka
//				   1999
//---------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class CombCanvas extends Canvas
    implements MouseListener, MouseMotionListener
{
    static final int horizPadding =KeybCanvas.horizPadding;
    static final int vertPadding  =15;

    static       int combHeight   =16;

    int width;
    int height;

    static final int COMB_NIL    =1;  // nothing moved
    static final int COMB_ORIGKEY=2;  // what is moving
    static final int COMB_FROM   =3;
    static final int COMB_TO     =4;
    int action=COMB_NIL;              // no action



    public KeybRecord ar=null;               // actuall record
    public KeybRecord arLeft=null;           // actuall record left neighb.
    public KeybRecord arRight=null;          // actuall record right neighb.



    public CombCanvas()
    {
        addMouseListener(this);
        addMouseMotionListener(this);

        width  =KeybCanvas.num*KeybCanvas.thickness +2*horizPadding;
        height =combHeight                          +2*vertPadding;
    }



    public void update(Graphics g)
    {
        paint(g);
    }



    public void paint(Graphics g)
    {
        int i;

        // get current size
        int w = getSize().width;
        int h = getSize().height;

        // recount parameters
        width  =KeybCanvas.num*KeybCanvas.thickness  +2*horizPadding;
        height =combHeight             +2*vertPadding;

        // white bg
        g.setColor(Color.white);
        g.fillRect(0, 0, w-1, h-1);

        // draw comb
        int ch2= combHeight/2;
        g.setColor(Color.lightGray);
        g.drawLine(horizPadding,                       vertPadding+ch2,
                   horizPadding+KeybCanvas.num*KeybCanvas.thickness, vertPadding+ch2);

        g.drawLine(horizPadding, vertPadding,
                   horizPadding, vertPadding+combHeight);
        g.drawLine(horizPadding+KeybCanvas.num*KeybCanvas.thickness, vertPadding,
                   horizPadding+KeybCanvas.num*KeybCanvas.thickness, vertPadding+combHeight);

        // records
        KeybRecord r;
        for(i=0; i<SampleMapping.records.size(); i++ )
        {
            r=(KeybRecord)SampleMapping.records.elementAt(i);
            paintRecord(r);
        }

    }



    public void newRecord()
    {
        // insert new record into record list
        KeybRecord r=new KeybRecord("aaa",
                                    SMStatus.note,
                                    SMStatus.note,
                                    SMStatus.note);

        paintRecord(r);
    }

    public void paintRecord(KeybRecord r)
    {
        Graphics g= ((Component)this).getGraphics();
        if(g!=null)
            r.draw(g);
    }



    public Dimension getMinimumSize()
    {
        return new Dimension(width,height);
    }
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }



    public void mousePressed(MouseEvent e)
    {
        // decide by position (origKey/over/under comb)
        if(e.getY() < (vertPadding+combHeight/2)) // over
        {
            // orig key is in padding (to is behind)
            if(e.getY() < vertPadding) // orig
                action=COMB_ORIGKEY;
            else
                action=COMB_FROM;
        }
        else // under
            action=COMB_TO;
    }

    public void mouseReleased(MouseEvent e)
    {
        if(!SampleMapping.lazy)
            SampleMapping.guru.recordsToTextArea();
    }

    public void mouseEntered(MouseEvent e)
    {}
    public void mouseExited(MouseEvent e)
    {}

    public void mouseClicked(MouseEvent e)
    {}

    // mouse motion
    public void mouseMoved(MouseEvent e)
    {
        if(SampleMapping.mouseUpdateEnabled)
        {
            float notef=((float)e.getX() -(float)horizPadding)/(float)KeybCanvas.thickness;
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
        float notef=((float)e.getX() -(float)horizPadding)/(float)KeybCanvas.thickness;
        int   note =(int)notef;
        if(note<0)
            note=0;
        int logNote=SMStatus.phys2Log(note);
        int i,j,k;


        if(ar!=null)
        {
        // actions
        switch(action)
        {
            case COMB_TO:
                if(notef==((float)note)) // check for cis,dis,...
                {
                    if(SMStatus.isBlackKey(logNote-1) && logNote>0)
                        logNote--;
                }
                ar.to=note;
                ar.logTo=logNote;
                if(ar.logTo<ar.logOrigKey) { ar.to=ar.origKey; ar.logTo=ar.logOrigKey; }

                // check right neigbour (must be: right.from>ar.to)
                i=SampleMapping.records.indexOf(ar);
                if((i+1) != SampleMapping.records.size()) // has right neighb.
                {
                    arRight=(KeybRecord)SampleMapping.records.elementAt(i+1);
                    if(ar.logTo>=arRight.logFrom)
                    {
                        // try to move right neigbour
                        if((ar.logTo+1)<arRight.logOrigKey)
                        {
                            arRight.logFrom=ar.logTo+1;
                            if(SMStatus.isBlackKey(ar.logTo))
                                arRight.from=ar.to;
                            else
                                arRight.from=ar.to+1;
                        }
                        else // move to maximum (logFrom=logOrigKey)
                        {
                            // from -> orig
                            arRight.from=arRight.origKey;
                            arRight.logFrom=arRight.logOrigKey;
                            // correct to
                            ar.logTo=arRight.logFrom-1;
                            if(SMStatus.isBlackKey(ar.logTo))
                                ar.to=arRight.from;
                            else
                                ar.to=arRight.from-1;
                        }
                    }
                }

                repaint();
                break;
            case COMB_FROM:
                if(notef==((float)note)) // check for cis,dis,...
                {
                    if(SMStatus.isBlackKey(logNote-1) && logNote>0)
                        logNote--;
                }
                ar.from=note;
                ar.logFrom=logNote;
                if(ar.from<0) {ar.from=0; ar.logFrom=0;}
                if(ar.logFrom>ar.logOrigKey) {ar.from=ar.origKey; ar.logFrom=ar.logOrigKey;}

                // check left neigbour (must be: left.to<ar.from)
                i=SampleMapping.records.indexOf(ar);
                if(i>0) // has right neighb.
                {
                    arLeft=(KeybRecord)SampleMapping.records.elementAt(i-1);
                    if(ar.logFrom<=arLeft.logTo)
                    {
                        // try to move left neigbour
                        if((ar.logFrom-1)>arLeft.logOrigKey)
                        {
                            arLeft.logTo=ar.logFrom-1;
                            if(SMStatus.isBlackKey(arLeft.logTo))
                                arLeft.to=ar.from;
                            else
                                arLeft.to=ar.from-1;
                        }
                        else // move to maximum (logTo=logOrigKey)
                        {
                            // to -> orig
                            arLeft.to=arLeft.origKey;
                            arLeft.logTo=arLeft.logOrigKey;
                            // correct From
                            ar.logFrom=arLeft.logTo+1;
                            if(SMStatus.isBlackKey(arLeft.logTo))
                                ar.from=arLeft.to;
                            else
                                ar.from=arLeft.to+1;
                        }
                    }
                }

                repaint();
                break;
            case COMB_ORIGKEY:
                if(notef==((float)note)) // check for cis,dis,...
                {
                    if(SMStatus.isBlackKey(logNote-1) && logNote>0)
                        logNote--;
                }
                ar.origKey=note;
                ar.logOrigKey=logNote;
                if(ar.logTo<ar.logOrigKey) {ar.to=ar.origKey; ar.logTo=ar.logOrigKey;}
                if(ar.logFrom>ar.logOrigKey) {ar.from=ar.origKey; ar.logFrom=ar.logOrigKey;}

                repaint();
                break;
            default:
        }
        }

        if(notef==((float)note))
            SampleMapping.smStatus.showNote(note, true); // check for cis,dis,...
        else
            SampleMapping.smStatus.showNote(note, false); // white key



        if(false)
            System.out.println("Mouse dragged");
    }

}

//- EOF -----------------------------------------------------------------------
