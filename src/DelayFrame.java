//-----------------------------------------------------------------------------
//			       Sample mapper
//                                Dvorka
//                                 2000
//-----------------------------------------------------------------------------

import java.awt.*;
import java.awt.event.*;

public class DelayFrame extends Frame
    implements ActionListener
{
    public static final boolean deBug=true;

    private Button  okButton,
                    resetButton,
                    recountButton;
    private Panel   p;   
    private TextField bpmL,
                      delay1L,
                      delay2L,
                      bpmR,
                      delay1R,
                      delay2R;
    private float     bpmLVal   =120,
                      delay1LVal=375,
                      delay2LVal=250,
                      bpmRVal   =110,
                      delay1RVal=0,
                      delay2RVal=0;

    private SampleMapping guru;
                
    public DelayFrame( SampleMapping guru )
    {
        this.guru=guru;

        GridLayout layout = new GridLayout(0,3,5,7); // row, col, gaps
        setLayout(layout); 
        setFont(new Font("Helvetica", Font.PLAIN, 12));

        for( int i=0; i<=8; i++ )
        {
            // create panel and set layout
            p = new Panel();
            p.setLayout( new FlowLayout(FlowLayout.CENTER,3,5)); // in panel

            switch( i )
            {
            case 0: 
                p.add( new Label("Bpm", Label.LEFT) ); 
                p.setLayout( new FlowLayout(FlowLayout.LEFT,3,5)); // in panel
                break;
            case 1: 
                bpmL = new TextField(""+(int)bpmLVal,5);
                bpmL.setEditable(false);
                p.add( bpmL );
                break;
            case 2: 
                bpmR = new TextField(""+(int)bpmRVal,5);
                bpmR.setEditable(true);
                p.add( bpmR );
                break;
            // -------
            case 3:
                p.add( new Label("Delay I.", Label.LEFT) ); // !!! to frame (gridlayout) add p -> will be spread by grid, because component is encapsulted it will have own size
                p.setLayout( new FlowLayout(FlowLayout.LEFT,3,5)); // in panel
                break;
            case 4:
                delay1L = new TextField(""+delay1LVal,5);
                delay1L.setEditable(true);
                p.add( delay1L );
                break;
            case 5:
                delay1R = new TextField(""+delay1RVal,5);
                delay1R.setEditable(false);
                p.add( delay1R );
                break;
            // -------
            case 6:
                p.add( new Label("Delay II.", Label.LEFT) ); // !!! to frame (gridlayout) add p -> will be spread by grid, because component is encapsulted it will have own size
                p.setLayout( new FlowLayout(FlowLayout.LEFT,3,5)); // in panel
                break;
            case 7:
                delay2L = new TextField(""+delay2LVal,5);
                delay2L.setEditable(true);
                p.add( delay2L );
                break;
            case 8:
                delay2R = new TextField(""+delay2RVal,5);
                delay2R.setEditable(false);
                p.add( delay2R );
                break;
            // -------
            default:
                break;
            }
            // add panel to grid layout
            add( p );
        }

        resetButton =new Button("Reset");
         resetButton.addActionListener(this);
         add( resetButton ); // into frame layout
        recountButton =new Button("ReCount");
         recountButton.addActionListener(this);
         add( recountButton ); // into frame layout
        okButton=new Button("OK");
         okButton.addActionListener(this);
         add( okButton ); // into frame layout



         addWindowListener( new WindowAdapter()
                            {
                                public void windowClosing(WindowEvent e)
                                {
                                    dispose();
                                    SampleMapping.delayFrame=null;
                                }
                            }
                          );
    }

    float recountDelay(float b1, float b2, float d1) // and return d2
    {
        return (float)(((int)(((b1/b2)*d1)*10.0))/10.0);
    }

    void printFields()
    {
        if(deBug)
        {
            System.out.println(
                               "\nbpmL   : "+bpmL.getText()+
                               "\ndelay1L: "+delay1L.getText()+
                               "\ndelay2L: "+delay2L.getText()+
                               "\nbpmR   : "+bpmR.getText()+
                               "\ndelay1R: "+delay1R.getText()+
                               "\ndelay2R: "+delay2R.getText()+
                               "\n----------------------------"+
                               "\nbpmL   : "+bpmLVal+
                               "\ndelay1L: "+delay1LVal+
                               "\ndelay2L: "+delay2LVal+
                               "\nbpmR   : "+bpmRVal+
                               "\ndelay1R: "+delay1RVal+
                               "\ndelay2R: "+delay2RVal);
        }
    }

    //- events ----------------------------------------------------------------

    // Action listener method
    public void actionPerformed(ActionEvent e)
    {

        if(e.getSource()==okButton)
        {
            dispose();
            SampleMapping.delayFrame=null;
        }
        else
            if(e.getSource()==resetButton)
            {
                bpmLVal   =120; bpmL.setText(""+(int)bpmLVal);
                delay1LVal=375; delay1L.setText(""+delay1LVal);
                delay2LVal=250; delay2L.setText(""+delay2LVal);
                bpmRVal   =110; bpmR.setText(""+(int)bpmRVal);
                delay1RVal=recountDelay(bpmLVal,bpmRVal,delay1LVal);
                delay2RVal=recountDelay(bpmLVal,bpmRVal,delay2LVal);

                delay1R.setText(""+delay1RVal);
                delay2R.setText(""+delay2RVal);

                if(deBug)
                    printFields();
            }
            else
                if(e.getSource()==recountButton)
                {
                    try { bpmRVal=Float.valueOf(bpmR.getText()).floatValue();}
                    catch(NumberFormatException exc) { bpmRVal=110; }
                    try { delay1LVal=Float.valueOf(delay1L.getText()).floatValue();}
                    catch(NumberFormatException exc) { delay1LVal=375; }
                    try { delay2LVal=Float.valueOf(delay2L.getText()).floatValue();}
                    catch(NumberFormatException exc) { delay2LVal=250; }

                    // recount values
                    delay1RVal=recountDelay(bpmLVal,bpmRVal,delay1LVal);
                    delay2RVal=recountDelay(bpmLVal,bpmRVal,delay2LVal);
                    delay1R.setText(""+delay1RVal);
                    delay2R.setText(""+delay2RVal);

                    if(deBug)
                        printFields();
                }
    }
}

//- EOF -----------------------------------------------------------------------
