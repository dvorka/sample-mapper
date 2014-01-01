//---------------------------------------------------------------------------
//                           Sample mapper tool
//                                Dvorka
//                                 2000
//---------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;
import java.io.*;



public class EnterNameFrame extends Frame
    implements ActionListener, KeyListener
{
    public  static boolean modify=false;     // use frame to update record
                                             // else to create new

    private Panel     p;   // panel encapsulates components
    private Button    okButton,
                      cancelButton;
    public  TextField sampleName;



    public EnterNameFrame()
    {
        SampleMapping.mouseUpdateEnabled=false;

        setLayout(new GridLayout(0,1,5,7));             // row, col, gaps
        setFont(new Font("Courier", Font.PLAIN, 12));



        // create panel and set layout
        p = new Panel();
        p.setLayout( new FlowLayout(FlowLayout.CENTER,3,5)); // in panel
        p.add( new Label("Sample:", Label.LEFT) ); // !!! to frame (gridlayout) add p -> will be spread by grid, because component is encapsulted it will have own size
        if(modify)
            sampleName = new TextField(SampleMapping.comb.ar.name,17);
        else
            sampleName = new TextField("Sample",17);
        sampleName.setEditable(true);
        sampleName.selectAll();
        p.add( sampleName );
        add( p ); 



        // for buttons
        p = new Panel();
        p.setLayout( new FlowLayout(FlowLayout.CENTER,3,5)); // in panel
        okButton =new Button("OK");
        okButton.addActionListener(this);
        p.add( okButton );
        cancelButton =new Button("Cancel");
        cancelButton.addActionListener(this);
        p.add( cancelButton );
        add( p );

        sampleName.addKeyListener(this);

        addWindowListener( new WindowAdapter()
                           {
                               public void windowClosing(WindowEvent e)
                               {
                                   dispose();
                               }
                           }
                         );
    }

    //-------------------------------------------------------------------------

    void okExit()
    {
            if(modify)
            {
                SampleMapping.comb.ar.name= sampleName.getText();
                SampleMapping.comb.repaint();
                SampleMapping.smStatus.repaint();
                SampleMapping.guru.recordsToTextArea();
            }
            else
            {
                SampleMapping.comb.ar= new KeybRecord(sampleName.getText(),
                                                      SMStatus.note,
                                                      SMStatus.note,
                                                      SMStatus.note);
                SampleMapping.records.addElement(SampleMapping.comb.ar);
                SampleMapping.comb.repaint();
                SampleMapping.smStatus.repaint();
                SampleMapping.guru.recordsToTextArea();
            }

            dispose();
            SampleMapping.mouseUpdateEnabled=true;
    }

    //- events ----------------------------------------------------------------

    // Action listener method
    public void actionPerformed(ActionEvent e)
    {

        if(e.getSource()==okButton)
        {
            okExit();
        }
        else
            if(e.getSource()==cancelButton)
            {
                dispose();
                SampleMapping.mouseUpdateEnabled=true;
            }

    }

    //- events ----------------------------------------------------------------

    public void keyPressed(KeyEvent e)
    {
        char c        = e.getKeyChar();
        int keyCode   = e.getKeyCode();
        int modifiers = e.getModifiers();

        switch(keyCode)
        {
            case 10: // enter
                okExit();
                break;
        }

        if(false)
        {
            String keyCodeString = KeyEvent.getKeyText(keyCode);
            System.out.println("X: "+c+"/"+keyCodeString+"/"+modifiers+"/");
        }
    }

    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e) {}

}
