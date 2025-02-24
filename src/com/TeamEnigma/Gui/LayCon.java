package com.TeamEnigma.Gui;
import javax.swing.*;
import java.awt.*;

public class LayCon extends JFrame{
    public Container container ;

    public void frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  this.setBounds(250, 180, 435, 420);
        this.setBounds(250, 180, 600, 600);
        this.setResizable(false);
    }

    public void container(){
        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(new Color(146, 141, 141));
    }
}
