//---------------------------------------------------------------------------
//		      JNaaga - the game (java application)
//                              by Dvorka
//				   1999
//---------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;

class SMStatus extends Canvas
{                                 
    public static int note=0;    // physical note (white keys) 
    public static int logNote=0; // both white and black keys

    int width=0;
    int height=30;

    Color bg = new Color(0xCAE1FF);



    public static String note2String(int pn,int ln)
    {
        int firstStupnice= -2;
        int stupnice=pn/7+firstStupnice;
        String nota=null;

        String cross=null;

        if(isBlackKey(ln))
        {
            cross=new String("#");

            switch(pn%7)
            {
                case 0:
                    nota=new String("H");
                    break;
                case 1:
                    nota=new String("C");
                    break;
                case 2:
                    nota=new String("D");
                    break;
                case 3:
                    nota=new String("E");
                    break;
                case 4:
                    nota=new String("F");
                    break;
                case 5:
                    nota=new String("G");
                    break;
                case 6:
                    nota=new String("A");
                    break;
            }

        }
        else
        {
            cross=new String(" ");

            switch(pn%7)
            {
                case 0:
                    nota=new String("C");
                    break;
                case 1:
                    nota=new String("D");
                    break;
                case 2:
                    nota=new String("E");
                    break;
                case 3:
                    nota=new String("F");
                    break;
                case 4:
                    nota=new String("G");
                    break;
                case 5:
                    nota=new String("A");
                    break;
                case 6:
                    nota=new String("H");
                    break;
            }
        }

        return (new String(" "+nota+" "+stupnice+cross));
    }


    public static boolean isBlackKey(int ln)
        // - for snifting if before me is black key
    {
        int logNote=0;

        // flags for halftones: pattern XX.XXX.
        boolean tripple= true; // switch when . comes
        int     halfoff= 3;    // 012 -> switch; 0123 -> switch

        // cycle and increment whenever it's needed
        for(int i=0; i<=ln; i++,logNote++)
        {
            // halftones
            if(tripple)
            {
                if(halfoff==3)
                { tripple=false; halfoff=0; if(ln==logNote) return false; }
                else
                {
                        if(ln==logNote) return true;
                        logNote++;halfoff++;
                        if(ln==logNote) return false;
                }
            }
            else
            {
                if(halfoff==2)
                { tripple=true; halfoff=0; if(ln==logNote) return false;}
                else
                {
                    if(ln==logNote) return true;
                    logNote++;halfoff++;
                    if(ln==logNote) return false;
                }
            }
        }

        return false;
    }



    public static int phys2Log(int pn)
    {
        int logNote=0;

        // flags for halftones: pattern XX.XXX.
        boolean tripple= true; // switch when . comes
        int     halfoff= 3;    // 012 -> switch; 0123 -> switch

        // cycle and increment whenever it's needed
        for(int i=0; i<=pn; i++,logNote++)
        {
            // halftones
            if(tripple)
            {
                if(halfoff==3)
                { tripple=false; halfoff=0; }
                else
                {
                    logNote++;
                    halfoff++;
                }
            }
            else
            {
                if(halfoff==2)
                { tripple=true; halfoff=0; }
                else
                {
                    logNote++;
                    halfoff++;
                }
            }
        }

        return --logNote;
    }



    void showNote(int pn, boolean check)
    {
        int ln=phys2Log(pn);
        if(check)
        {
            if(isBlackKey(ln-1) && ln>0)
                ln--;
        }

        if(ln!=logNote)
        {
            note=pn; logNote=ln;
            repaint();
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        int lineh=13;

        // get current size
        int w = getSize().width;
        int h = getSize().height;

        g.setColor(bg);
        g.fillRect(0, 0, w-1, h);

        g.setColor(Color.black);
        g.drawString( "Note: "+note2String(note,logNote)
                      +"   ("+note+"/"+logNote+")"
                     ,10,lineh);

        if(SampleMapping.comb.ar!=null)
            g.drawString("Active record is \""
                         +SampleMapping.comb.ar.name+"\": "
                         +note2String(SampleMapping.comb.ar.origKey,SampleMapping.comb.ar.logOrigKey)
                         +" | "
                         +note2String(SampleMapping.comb.ar.from,SampleMapping.comb.ar.logFrom)
                         +" . "
                         +note2String(SampleMapping.comb.ar.to,SampleMapping.comb.ar.logTo)
                         +" "
                         ,10,lineh*2);
    }



    public Dimension getMinimumSize()
    {
        return new Dimension(width,height);
    }
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }
}

//- EOF -----------------------------------------------------------------------
