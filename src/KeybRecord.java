//---------------------------------------------------------------------------
//		      JNaaga - the game (java application)
//                              by Dvorka
//				   1999
//---------------------------------------------------------------------------

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class KeybRecord
{
    public Color c;             // orig key color

    public String name;
    public int origKey,
               from,
               to;
    public int logOrigKey,
               logFrom,
               logTo;



    public void save(DataOutputStream out)
    {
        try
        {
            out.writeUTF(name);
            out.writeInt(origKey);
            out.writeInt(from);
            out.writeInt(to);
            out.writeInt(logOrigKey);
            out.writeInt(logFrom);
            out.writeInt(logTo);
        }
        catch(IOException e) {}
    }



    public void read(DataInputStream in)
    {
        try
        {
            name=in.readUTF();
            origKey=in.readInt();
            from=in.readInt();
            to=in.readInt();
            logOrigKey=in.readInt();
            logFrom=in.readInt();
            logTo=in.readInt();
        }
        catch(IOException e) {}
    }



    public KeybRecord(String name, int origKey, int from, int to)
    {
        this.name=name;

        this.logOrigKey=SMStatus.phys2Log(origKey);
        this.origKey=origKey;

        this.logFrom=SMStatus.phys2Log(from);
        this.from=from;

        this.logTo=SMStatus.phys2Log(to);
        this.to=to;
    }


    void downTriangle(Graphics g,int top)
    {
        if(SampleMapping.comb.ar==this)
            g.setColor(Color.blue);
        else
            g.setColor(Color.black);

        // x-y
        //  z
        int[] xs=
        {
            top-KeybCanvas.thickness/2, // x
            top+KeybCanvas.thickness/2, // y
            top                         // z
        };
        int[] ys=
        {
            CombCanvas.vertPadding,
            CombCanvas.vertPadding,
            CombCanvas.vertPadding+CombCanvas.combHeight/2
        };

        g.fillPolygon(xs,ys,xs.length);
    }                                          
    void upTriangle(Graphics g,int top)
    {
        if(SampleMapping.comb.ar==this)
            g.setColor(Color.blue);
        else
            g.setColor(Color.black);

        //  z
        // x-y
        int[] xs=
        {
            top-KeybCanvas.thickness/2,
            top+KeybCanvas.thickness/2,
            top
        };
        int[] ys=
        {
            CombCanvas.vertPadding+CombCanvas.combHeight +1,
            CombCanvas.vertPadding+CombCanvas.combHeight +1,
            CombCanvas.vertPadding+CombCanvas.combHeight/2 
        };

        g.fillPolygon(xs,ys,xs.length);
    }
    void origKeyTriangle(Graphics g,int top)
    {
        if(SampleMapping.comb.ar==this)
            g.setColor(Color.red);
        else
            g.setColor(Color.orange);

        //  z
        // x-y

        int[] xs=
        {
            top-KeybCanvas.thickness/2, // x
            top+KeybCanvas.thickness/2, // y
            top                         // z
        };                      
        int[] ys=
        {
            CombCanvas.combHeight/2,
            CombCanvas.combHeight/2,
            0
        };

        g.fillPolygon(xs,ys,xs.length);
    }



    public void draw(Graphics g)
    {
        int toTop, fromTop;

        if(SMStatus.isBlackKey(logTo))
            toTop=CombCanvas.horizPadding+to*KeybCanvas.thickness;
        else
            toTop=CombCanvas.horizPadding+to*KeybCanvas.thickness+KeybCanvas.thickness/2;
        upTriangle(g, toTop);

        if(SMStatus.isBlackKey(logFrom))
            fromTop=CombCanvas.horizPadding+from*KeybCanvas.thickness;
        else
            fromTop=CombCanvas.horizPadding+from*KeybCanvas.thickness+KeybCanvas.thickness/2;
        downTriangle(g, fromTop);

        if(SMStatus.isBlackKey(logOrigKey))
            origKeyTriangle(g, CombCanvas.horizPadding+origKey*KeybCanvas.thickness);
        else
            origKeyTriangle(g, CombCanvas.horizPadding+origKey*KeybCanvas.thickness+KeybCanvas.thickness/2);

        // connection
        if(SampleMapping.comb.ar==this)
            g.setColor(Color.blue);
        else
            g.setColor(Color.black);
        //g.drawLine(fromTop,CombCanvas.vertPadding+CombCanvas.combHeight/2,toTop,CombCanvas.vertPadding+CombCanvas.combHeight/2);
        g.fillRect(fromTop,CombCanvas.vertPadding+CombCanvas.combHeight/2,toTop-fromTop,2);
    }
}

//- EOF -----------------------------------------------------------------------
