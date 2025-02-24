package com.TeamEnigma.Gui;

import com.TeamEnigma.cognito.Tester;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame  {


    private JButton Button[];
    private GridLayout Layout;
    private JPanel Panel;
    private Font font;
    private JLabel Label;

    Gui() {

        initcomponent();
    }

    private void initcomponent() {
        Container container = this.getContentPane();
        container.setLayout(null);

        Font font = new Font("Arial", Font.BOLD, 18);

       Label = new JLabel("Are you ready for voice recording ?");
        Label.setBounds(15, 5, 415, 55);
        Label.setFont(font);
        Label.setBackground(Color.DARK_GRAY);
        container.add(Label);

        Layout = new GridLayout(5, 4);
        Panel = new JPanel();
        Panel.setBounds(15, 122, 415, 370);
        Panel.setLayout(Layout);

        container.add(Panel);


     Button[9] = new JButton("Train");
        Panel.add(Button[9]);



         Button[9].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Tester m = new Tester();
            }

        });

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(800, 250, 460, 550);
        this.setTitle(" Center Finder");

    }
     public static void main(String[] args) {
        Gui k = new Gui();
        k.setVisible(true);

    }
}