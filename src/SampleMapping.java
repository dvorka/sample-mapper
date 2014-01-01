//---------------------------------------------------------------------------
//		           Sample mapper (JDK 1.1)
//                                Dvorka
//                                 2000
//---------------------------------------------------------------------------
// ToDo:
// - prohozeni osetreni udalosti menu

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;



public class SampleMapping extends Frame
    implements ActionListener, ItemListener
{
    public static              SampleMapping guru;
    public static boolean      lazy       = false;      // lazy refresh
    public static AboutFrame   aboutFrame = null;
    public static DelayFrame   delayFrame = null;
    public static String       newline;
    public static boolean      mouseUpdateEnabled=true;
    boolean                    inAnApplet = true;

    MenuBar  mb;
    Menu     mf, mh, mt;
    Button   newButton, loadButton, saveButton, quitButton,
             refreshButton, modifyButton, deleteButton;
    public static KeybCanvas keyboard = new KeybCanvas();
    public static CombCanvas comb     = new CombCanvas();
    public static SMStatus   smStatus = new SMStatus();
    public static List       textRecords = new List(15);
    public static Choice     lazyChoice  = new Choice();

    public static Vector records= new Vector();

    public SampleMapping()
    {
        guru    = this;
        newline = System.getProperty("line.separator");

        setLayout(new BorderLayout());                  // for this frame

        // bottom panel
        // status panel



        // bottom panel
        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new GridLayout(2,1)); // rows, cols
        bottomPanel.add(keyboard);
        bottomPanel.add(comb);
        add("North", bottomPanel);                      // add it to frame

        // status
        Panel statusPanel = new Panel();
        statusPanel.setLayout(new BorderLayout());     
         textRecords.addItemListener(this);
         textRecords.setFont(new Font("Courier",Font.PLAIN,12));
        statusPanel.add("Center",textRecords);
        statusPanel.add("South",smStatus);
        Panel buttonPanel=new Panel();
         buttonPanel.setLayout(new GridLayout(8,1));    // rows, cols
          lazyChoice.add("EarlyFresh");
          lazyChoice.add("LazyFresh");
          lazyChoice.addItemListener(this);
         buttonPanel.add(lazyChoice);
          newButton    =new Button("New");     newButton.addActionListener(this);     buttonPanel.add(newButton);
          refreshButton=new Button("Refresh"); refreshButton.addActionListener(this); buttonPanel.add(refreshButton);
          loadButton   =new Button("Load");    loadButton.addActionListener(this);    buttonPanel.add(loadButton);
          saveButton   =new Button("Save");    saveButton.addActionListener(this);    buttonPanel.add(saveButton);
          modifyButton =new Button("Modify");  modifyButton.addActionListener(this);  buttonPanel.add(modifyButton);
          deleteButton =new Button("Delete");  deleteButton.addActionListener(this);  buttonPanel.add(deleteButton);
          quitButton   =new Button("Quit");    quitButton.addActionListener(this);    buttonPanel.add(quitButton);
        statusPanel.add("East",buttonPanel);
        add("South", statusPanel);                      // add it to frame



        // menu bar
        mb = new MenuBar();
         // file menu
         mf = new Menu("File");
         mf.addActionListener(this);
         mf.add(new MenuItem("New"));
         mf.add(new MenuItem("Load"));
         mf.add(new MenuItem("Save"));
         mf.add(new MenuItem("-"));
         mf.add(new MenuItem("Quit"));
        mb.add(mf);
         // tool menu
         mt = new Menu("Tools");
         mt.addActionListener(this);
         mt.add(new MenuItem("Delay"));
        mb.add(mt);
         // help menu
         mh = new Menu("Help");
         mh.addActionListener(this);
         mh.add(new MenuItem("About"));
        mb.setHelpMenu(mh);
        setMenuBar(mb);



	addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (inAnApplet) {
                    dispose();
                } else {
                    System.exit(0);
                }
            }
	});
    }


    // actions
    void actionNew()
    {
        records.removeAllElements();
        textRecords.removeAll();
        comb.repaint();
        comb.ar=null;
        smStatus.repaint();
    }
    void actionLoad()
    {
        // clear old
        records.removeAllElements();

        FileDialog fd = new FileDialog(this,"Load bank",FileDialog.LOAD);
        fd.setVisible(true);
        while(fd.isVisible()) ;

        KeybRecord r=null;
        DataInputStream in;
        int j;
        String f=fd.getFile();
        if(f!=null)
        {
            try
            {
                in= new DataInputStream(new FileInputStream(f));
                j=in.readInt();
                for(int i=0; i<j; i++)
                {
                    r= new KeybRecord("",0,0,0);
                    r.read(in);
                    records.addElement(r);
                }
                in.close();
            }
            catch(IOException exc) {}

            comb.ar=r;
            recordsToTextArea();
            comb.repaint();
            smStatus.repaint();
        }
    }
    void actionSave()
    {
        FileDialog fd = new FileDialog(this,"Save bank",FileDialog.SAVE);
        fd.setVisible(true);
        while(fd.isVisible()) ;

        KeybRecord r;
        DataOutputStream out;
        String f=fd.getFile();
        if(f!=null)
        {
            try
            {
                out= new DataOutputStream(new FileOutputStream(f));
                // save size
                out.writeInt(records.size());
                for(int i=0; i<records.size(); i++)
                {
                    r=(KeybRecord)records.elementAt(i);
                    r.save(out);
                }
                out.close();
            }
            catch(IOException exc) {}
        }
    }
    void actionQuit()
    {
        setVisible(false);
        dispose();
        System.exit(0);
    }
    void actionDelete()
    {
        if(comb.ar!=null)
        {
            records.removeElement(comb.ar);
            try
            {
                comb.ar=(KeybRecord)records.firstElement();
            }
            catch(NoSuchElementException exc)
            {
                comb.ar=null;
            }
            comb.repaint();
            smStatus.repaint();
            recordsToTextArea();
        }

    }
    void actionModify()
    {
        if(comb.ar!=null)
        {
            // enter sample name and create new record
            EnterNameFrame.modify=true; // create new record (not modify)
            EnterNameFrame enf=new EnterNameFrame();
            enf.setTitle("Modify sample name");
            enf.pack();

            // now put the frame to the center of the total screen
            Dimension ddww = enf.getSize();   // get size of frame after pack
            // get size of screen
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            // move window to the center of global screen
            enf.setLocation(new Point((screen.width-ddww.width)/2,(screen.height-ddww.height)/2));
            enf.setVisible(true);
        }
    }


    // Action listener method
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==mh)
        {
            if((e.getActionCommand()).compareTo(new String("About"))==0)
            {
                if( aboutFrame==null )
                {
                    aboutFrame = new AboutFrame();
                    aboutFrame.setTitle("About Sample mapper");
                    aboutFrame.pack();
                    Dimension ddww = aboutFrame.getSize();
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    aboutFrame.setLocation(new Point((screen.width-ddww.width)/2,(screen.height-ddww.height)/2));
                    aboutFrame.setVisible(true);
                }
            }
        }
        else
        if(e.getSource()==mt)
        {
            if((e.getActionCommand()).compareTo(new String("Delay"))==0)
            {
                if( delayFrame==null )
                {
                    delayFrame = new DelayFrame(this);
                    delayFrame.setTitle("Delay Calculator");
                    delayFrame.pack();
                    Dimension ddww = delayFrame.getSize();
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    delayFrame.setLocation(new Point((screen.width-ddww.width)/2,(screen.height-ddww.height)/2));
                    delayFrame.setVisible(true);
                }
            }
        }
        else
            if(e.getSource()==mf)
            {
                if((e.getActionCommand()).compareTo(new String("New"))==0)
                    actionNew();
                else
                    if((e.getActionCommand()).compareTo(new String("Load"))==0)
                        actionLoad();
                    else
                        if((e.getActionCommand()).compareTo(new String("Save"))==0)
                            actionSave();
                        else
                            if((e.getActionCommand()).compareTo(new String("Quit"))==0)
                                actionQuit();
            }
            else
                if(e.getSource()==newButton)
                    actionNew();
                else
                    if(e.getSource()==loadButton)
                        actionLoad();
                    else
                        if(e.getSource()==saveButton)
                            actionSave();
                        else
                            if(e.getSource()==modifyButton)
                                actionModify();
                            else
                                if(e.getSource()==deleteButton)
                                    actionDelete();
                                else
                                    if(e.getSource()==quitButton)
                                        actionQuit();
                                    else
                                        if(e.getSource()==refreshButton)
                                        {
                                            comb.repaint();
                                            smStatus.repaint();
                                            recordsToTextArea();
                                        }

        if(false) // debug
        {
            System.out.println("\"" + e.getActionCommand()
                               + "\" action detected in menu labeled \""
                               //                               + ((MenuItem)(e.getSource())).getLabel()
                               + e.getSource()
                               + "\"."
                               + "id: "
                               + e.getID()
                              );
        }
    }



    // Item listener method
    public void itemStateChanged(ItemEvent e)
    {
        if(e.getSource()==textRecords)
        {
            if(textRecords.getSelectedIndex()>=3)
                comb.ar=(KeybRecord)records.elementAt(textRecords.getSelectedIndex()-3);

            comb.repaint();
        }
        else
            if(e.getSource()==lazyChoice)
            {
                if(((String)e.getItem()).compareTo(new String("LazyFresh"))==0)
                    lazy=true;
                else
                    lazy=false;
            }



        if(false)
            System.out.println("item: "+e.getItem()+"/"+e.getSource());
    }



    public void sortRecords()
        // key is logTo (is biggest inside record and records are not croslinked)
    {
        // make shaker sort (only from left to right switch neighbours)

        if(records.size()>1)
        {
            KeybRecord rl,rr;
            boolean change=true;

            while(change)
            {
                change=false;

                for(int i=0; i<records.size()-1; i++)
                {
                    rl=(KeybRecord)records.elementAt(i);
                    rr=(KeybRecord)records.elementAt(i+1);

                    if(rl.logTo>rr.logTo) // switch neighbours
                    {
                        records.setElementAt(rr,i);
                        records.setElementAt(rl,i+1);
                        change=true;
                    }
                }
            }
        }

    }



    public void recordsToTextArea()
    {
        int j;
        KeybRecord r;
        String s;

        sortRecords();

        textRecords.removeAll();
        textRecords.add("+-------------------------------------------------------------------+");
        textRecords.add("| OrigKey |    From     To       |   Name                           |");
        textRecords.add("+-------------------------------------------------------------------+");

        for(int i=0; i<records.size(); i++)
        {
            r=(KeybRecord)records.elementAt(i);

            s="|"+smStatus.note2String(r.origKey,r.logOrigKey);
             for(j=s.length(); j<10; j++) s=s+" ";
            s=s +"|   "
                +smStatus.note2String(r.from,r.logFrom);
             for(j=s.length(); j<21; j++) s=s+" ";
            s=s +". "
                +smStatus.note2String(r.to,r.logTo);
             for(j=s.length(); j<33; j++) s=s+" ";
            s=s +"|   "
                +r.name;
                
            textRecords.add(s);
        }

        // select it in list
        if(comb.ar!=null)
            textRecords.select(records.indexOf(comb.ar)+3);
    }


    public static void main(String[] args)
    {
        SampleMapping window = new SampleMapping();
        window.inAnApplet = false;

        window.setTitle("Sample mapping tool");
        window.pack();

        // now put the frame to the center of the total screen
        Dimension ddww = window.getSize();   // get size of frame after pack
        // get size of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        // move window to the center of global screen
        window.setLocation(new Point((screen.width-ddww.width)/2,(screen.height-ddww.height)/2));

        window.setVisible(true);
    }

}

//- EOF -----------------------------------------------------------------------
