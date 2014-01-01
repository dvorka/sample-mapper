//---------------------------------------------------------------------------
//			    Sample mapper tool
//                               Dvorka
//                                2000
//---------------------------------------------------------------------------

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



public class AboutFrame extends Frame
    implements ActionListener
{
    private Button    button;
    
    public AboutFrame()
    {
        // constructor called auto
        setLayout(new GridLayout(5,1,1,1)); // row, col, gaps
        setFont(new Font("Courier", Font.PLAIN, 12));

        add( new Label( "            Sample mapper           ", Label.CENTER ) );
        add( new Label( "mailto: UltraDvorka@post.cz", Label.CENTER ) );
        add( new Label( "http://run.to/ultradvorka", Label.CENTER ) );
        add( new Label( "2000", Label.CENTER ) );

        button = new Button( "OK" );
        button.addActionListener(this);
        Panel p = new Panel();       // encapsulate button to panel
        p.setLayout( new FlowLayout(FlowLayout.CENTER, 3, 5));
        p.add( new Panel() );
        p.add( button );
        p.add( new Panel() );
        add(p);



        addWindowListener( new WindowAdapter()
                           {
                               public void windowClosing(WindowEvent e)
                               {
                                   dispose();
                               }
                           }
                         );
    }

    //- events ----------------------------------------------------------------

    // Action listener method
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==button)
        {
            setVisible(false);
            dispose();
            SampleMapping.aboutFrame=null;
        }
    }
}
